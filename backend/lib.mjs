export function approvedIds(value) {
  return new Set(
    String(value).split(",")
      .map(entry => Number(entry.trim()))
      .filter(entry => Number.isSafeInteger(entry) && entry > 0)
  );
}

export function validVerseKey(value) {
  if (typeof value !== "string") return false;
  const match = /^(\d{1,3}):(\d{1,3})$/.exec(value);
  if (!match) return false;
  const chapter = Number(match[1]);
  const verse = Number(match[2]);
  return chapter >= 1 && chapter <= 114 && verse >= 1 && verse <= 286;
}

export function normalizeAudioUrl(value) {
  if (typeof value !== "string" || !value.trim()) return null;
  try {
    const url = new URL(value, "https://verses.quran.foundation/");
    if (url.protocol !== "https:" || url.hostname !== "verses.quran.foundation" || url.username || url.password) {
      return null;
    }
    return url.toString();
  } catch {
    return null;
  }
}
