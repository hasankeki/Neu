import { createServer } from "node:http";

const PORT = Number(process.env.PORT || 8080);
const QF_CLIENT_ID = process.env.QF_CLIENT_ID;
const QF_CLIENT_SECRET = process.env.QF_CLIENT_SECRET;
const QF_API_BASE = process.env.QF_API_BASE || "https://apis.quran.foundation";
const QF_OAUTH_BASE = process.env.QF_OAUTH_BASE || "https://oauth2.quran.foundation";

let cachedToken = null;
let tokenExpiresAt = 0;

function sendJson(response, status, payload) {
  response.writeHead(status, {
    "content-type": "application/json; charset=utf-8",
    "cache-control": "no-store",
    "x-content-type-options": "nosniff"
  });
  response.end(JSON.stringify(payload));
}

function validVerseKey(value) {
  return typeof value === "string" && /^(?:[1-9]|[1-9][0-9]|1[01][0-9]|12[0-3]):[1-9][0-9]{0,2}$/.test(value);
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
      "content-type": "application/x-www-form-urlencoded"
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

async function qfGet(path) {
  const token = await getAccessToken();
  const response = await fetch(`${QF_API_BASE}/content/api/v4${path}`, {
    headers: {
      "x-auth-token": token,
      "x-client-id": QF_CLIENT_ID,
      accept: "application/json"
    }
  });
  if (!response.ok) throw new Error(`Quran Foundation content request failed (${response.status}).`);
  return response.json();
}

function normalizeAudioUrl(value) {
  if (typeof value !== "string" || !value.trim()) return null;
  const url = new URL(value, "https://verses.quran.foundation/");
  const allowedHosts = new Set(["verses.quran.foundation", "audio.qurancdn.com"]);
  if (url.protocol !== "https:" || !allowedHosts.has(url.hostname)) return null;
  return url.toString();
}

const server = createServer(async (request, response) => {
  try {
    const url = new URL(request.url || "/", `http://${request.headers.host || "localhost"}`);

    if (request.method === "GET" && url.pathname === "/health") {
      return sendJson(response, 200, { ok: true, service: "nurwallpaper-audio-api" });
    }

    if (request.method === "GET" && url.pathname === "/v1/reciters") {
      const data = await qfGet("/resources/recitations?language=en");
      const reciters = (data.recitations || []).map(item => ({
        id: Number(item.id),
        name: String(item.reciter_name || item.translated_name?.name || "Unbekannter Rezitator"),
        style: item.style || null
      })).filter(item => Number.isInteger(item.id) && item.id > 0 && item.name);
      return sendJson(response, 200, { reciters, source: "Quran Foundation" });
    }

    if (request.method === "GET" && url.pathname === "/v1/audio") {
      const verseKey = url.searchParams.get("verse_key");
      const recitationId = Number(url.searchParams.get("recitation_id"));
      if (!validVerseKey(verseKey) || !Number.isInteger(recitationId) || recitationId <= 0) {
        return sendJson(response, 400, { error: "A valid verse_key and recitation_id are required." });
      }

      const data = await qfGet(`/quran/recitations/${recitationId}?${new URLSearchParams({ verse_key: verseKey })}`);
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
    // Never expose tokens, credentials, or upstream response bodies to clients.
    const message = error instanceof Error ? error.message : "Unexpected server error.";
    const status = message.includes("not configured") ? 503 : 502;
    return sendJson(response, status, { error: message });
  }
});

server.listen(PORT, "0.0.0.0", () => {
  console.log(`NurWallpaper audio API listening on port ${PORT}`);
});
