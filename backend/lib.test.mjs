import test from "node:test";
import assert from "node:assert/strict";
import { approvedIds, normalizeAudioUrl, validVerseKey } from "./lib.mjs";

test("accepts valid chapter:verse keys", () => {
  assert.equal(validVerseKey("1:1"), true);
  assert.equal(validVerseKey("114:6"), true);
  assert.equal(validVerseKey("2:255"), true);
});

test("rejects malformed and out-of-range verse keys", () => {
  for (const value of ["0:1", "115:1", "1:0", "2:999", "2", "2:1:3", "", null]) {
    assert.equal(validVerseKey(value), false, String(value));
  }
});

test("only positive integer recitation IDs are approved", () => {
  assert.deepEqual([...approvedIds("7, 11,0,-1,abc,7")], [7, 11]);
});

test("normalizes relative verse audio URLs", () => {
  assert.equal(
    normalizeAudioUrl("Alafasy/mp3/001001.mp3"),
    "https://verses.quran.foundation/Alafasy/mp3/001001.mp3"
  );
});

test("rejects non-HTTPS and untrusted audio hosts", () => {
  assert.equal(normalizeAudioUrl("http://verses.quran.foundation/a.mp3"), null);
  assert.equal(normalizeAudioUrl("https://example.com/a.mp3"), null);
  assert.equal(normalizeAudioUrl("https://verses.quran.foundation.evil.test/a.mp3"), null);
  assert.equal(normalizeAudioUrl("https://user:pass@verses.quran.foundation/a.mp3"), null);
});
