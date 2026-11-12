import { createServer } from "node:http";
import { approvedIds, normalizeAudioUrl, validVerseKey } from "./lib.mjs";

const PORT = Number(process.env.PORT || 8080);
const QF_CLIENT_ID = process.env.QF_CLIENT_ID;
const QF_CLIENT_SECRET = process.env.QF_CLIENT_SECRET;
const QF_ENV = process.env.QF_ENV || "prelive";
const ENVIRONMENTS = {
  prelive: {
    api: "https://apis-prelive.quran.foundation",
    oauth: "https://prelive-oauth2.quran.foundation"
  },
  production: {
    api: "https://apis.quran.foundation",
    oauth: "https://oauth2.quran.foundation"
  }
};
if (!ENVIRONMENTS[QF_ENV]) throw new Error("QF_ENV must be prelive or production.");
const QF_API_BASE = process.env.QF_API_BASE || ENVIRONMENTS[QF_ENV].api;
const QF_OAUTH_BASE = process.env.QF_OAUTH_BASE || ENVIRONMENTS[QF_ENV].oauth;
if (![QF_API_BASE, QF_OAUTH_BASE].every(value => value.startsWith("https://"))) {
  throw new Error("Quran Foundation API and OAuth URLs must use HTTPS.");
}
const APPROVED_RECITATION_IDS = approvedIds(process.env.QF_APPROVED_RECITATION_IDS || "");

let cachedToken = null;
let tokenExpiresAt = 0;
const rateBuckets = new Map();
const RATE_WINDOW_MS = 60_000;
const RATE_LIMIT = 60;

function sendJson(response, status, payload) {
  response.writeHead(status, {
    "content-type": "application/json; charset=utf-8",
    "cache-control": "no-store",
    "x-content-type-options": "nosniff"
  });
  response.end(JSON.stringify(payload));
}

function isRateLimited(request) {
  const now = Date.now();
  const key = request.socket.remoteAddress || "unknown";
  let bucket = rateBuckets.get(key);
  if (!bucket || now - bucket.startedAt >= RATE_WINDOW_MS) {
    bucket = { startedAt: now, count: 0 };
    rateBuckets.set(key, bucket);
  }
  bucket.count += 1;
  if (rateBuckets.size > 5000) {
    for (const [address, value] of rateBuckets) {
      if (now - value.startedAt >= RATE_WINDOW_MS) rateBuckets.delete(address);
    }
  }
  return bucket.count > RATE_LIMIT;
}

async function getAccessToken() {
  if (!QF_CLIENT_ID || !QF_CLIENT_SECRET) {
    throw new Error("Quran Foundation server credentials are not configured.");
  }
  if (cachedToken && Date.now() < tokenExpiresAt - 60_000) return cachedToken;

  const credentials = Buffer.from(`${QF_CLIENT_ID}:${QF_CLIENT_SECRET}`).toString("base64");
  const response = await fetch(`${QF_OAUTH_BASE}/oauth2/token`, {
    method: "POST",
    headers: {
      authorization: `Basic ${credentials}`,
      "content-type": "application/x-www-form-urlencoded",
      "user-agent": "NurWallpaper-Audio-API/0.1"
    },
    body: new URLSearchParams({
      grant_type: "client_credentials",
      scope: "content.audio.read"
    })
  });
  if (!response.ok) throw new Error(`Quran Foundation authentication failed (${response.status}).`);
  const body = await response.json();
  if (!body.access_token) throw new Error("Quran Foundation returned no access token.");

  cachedToken = body.access_token;
  tokenExpiresAt = Date.now() + Number(body.expires_in || 3600) * 1000;
  return cachedToken;
}

async function qfGet(path, retried = false) {
  const token = await getAccessToken();
  const response = await fetch(`${QF_API_BASE}/content/api/v4${path}`, {
    headers: {
      "x-auth-token": token,
      "x-client-id": QF_CLIENT_ID,
      accept: "application/json",
      "user-agent": "NurWallpaper-Audio-API/0.1"
    }
  });
  if (response.status === 401 && !retried) {
    cachedToken = null;
    tokenExpiresAt = 0;
    return qfGet(path, true);
  }
  if (!response.ok) throw new Error(`Quran Foundation content request failed (${response.status}).`);
  return response.json();
}

const server = createServer(async (request, response) => {
  try {
    const url = new URL(request.url || "/", `http://${request.headers.host || "localhost"}`);

    if (request.method === "GET" && url.pathname === "/health") {
      return sendJson(response, 200, { ok: true, service: "nurwallpaper-audio-api" });
    }
    if (isRateLimited(request)) {
      response.setHeader("retry-after", "60");
      return sendJson(response, 429, { error: "Too many requests. Please try again later." });
    }

    if (request.method === "GET" && url.pathname === "/v1/reciters") {
      const data = await qfGet("/resources/recitations?language=en");
      const reciters = (data.recitations || []).filter(item =>
        APPROVED_RECITATION_IDS.has(Number(item.id))
      ).map(item => ({
        id: Number(item.id),
        name: String(item.reciter_name || item.translated_name?.name || "Unbekannter Rezitator"),
        style: item.style || null
      })).filter(item => Number.isInteger(item.id) && item.id > 0 && item.name);
      return sendJson(response, 200, {
        reciters,
        source: "Quran Foundation",
        message: reciters.length ? undefined : "No reciters have been approved for this application yet."
      });
    }

    if (request.method === "GET" && url.pathname === "/v1/audio") {
      const verseKey = url.searchParams.get("verse_key");
      const recitationId = Number(url.searchParams.get("recitation_id"));
      if (!validVerseKey(verseKey) || !Number.isInteger(recitationId) || recitationId <= 0) {
        return sendJson(response, 400, { error: "A valid verse_key and recitation_id are required." });
      }
      if (!APPROVED_RECITATION_IDS.has(recitationId)) {
        return sendJson(response, 403, { error: "This reciter is not approved for this application." });
      }

      const data = await qfGet(`/audio/recitations/${recitationId}?${new URLSearchParams({ verse_key: verseKey })}`);
      const match = (data.audio_files || []).find(item => item.verse_key === verseKey);
      const audioUrl = normalizeAudioUrl(match?.url);
      if (!audioUrl) return sendJson(response, 404, { error: "No approved audio URL was returned for this verse." });

      return sendJson(response, 200, {
        verse_key: verseKey,
        recitation_id: recitationId,
        audio_url: audioUrl,
        source: "Quran Foundation"
      });
    }

    return sendJson(response, 404, { error: "Not found." });
  } catch (error) {
    const message = error instanceof Error ? error.message : "Unexpected server error.";
    const status = message.includes("not configured") ? 503 : 502;
    return sendJson(response, status, { error: message });
  }
});

server.listen(PORT, "0.0.0.0", () => {
  console.log(`NurWallpaper audio API listening on port ${PORT} (${QF_ENV})`);
});
