#!/usr/bin/env python3
"""Validate NurWallpaper's bundled content catalogue and release gate."""
import json
from pathlib import Path

CATALOGUE = Path("app/src/main/assets/content-library.json")
ALLOWED_CATEGORIES = {"VERSE", "HADITH", "DUA_DHIKR", "ASMA_UL_HUSNA"}
ALLOWED_STATUSES = {"RESEARCH", "DRAFT", "IN_REVIEW", "APPROVED_FOR_RELEASE"}
ALLOWED_HADITH_COLLECTIONS = {"Sahih al-Bukhari", "Sahih Muslim"}
REQUIRED_LANGUAGES = {"de", "tr", "en"}


def main() -> None:
    data = json.loads(CATALOGUE.read_text(encoding="utf-8"))
    assert data.get("schemaVersion") == 1, "Unsupported catalogue schema"
    items = data.get("items")
    assert isinstance(items, list), "items must be an array"

    ids = [item.get("id") for item in items]
    assert all(ids), "Every item needs an id"
    assert len(ids) == len(set(ids)), "Duplicate content IDs"

    approved = []
    pending = []
    for item in items:
        assert item.get("category") in ALLOWED_CATEGORIES, item.get("id")
        assert item.get("reviewStatus") in ALLOWED_STATUSES, item.get("id")
        assert item.get("originalLanguageCode", "ar") == "ar", item.get("id")
        assert item.get("originalText", "").strip(), item.get("id")
        source = item.get("source", {})
        assert source.get("collection", "").strip(), item.get("id")
        assert source.get("locator", "").strip(), item.get("id")
        assert source.get("sourceUrl", "").startswith("https://"), item.get("id")
        assert source.get("license", "").strip(), item.get("id")
        assert source.get("attribution", "").strip(), item.get("id")

        if item["category"] == "HADITH":
            assert source["collection"] in ALLOWED_HADITH_COLLECTIONS, item.get("id")

        translations = item.get("translations", [])
        if item["reviewStatus"] == "APPROVED_FOR_RELEASE":
            languages = {t.get("languageCode") for t in translations}
            assert languages == REQUIRED_LANGUAGES, item.get("id")
            assert len(translations) == 3, item.get("id")
            for translation in translations:
                for field in ("text", "translator", "edition", "version", "license", "attribution"):
                    assert translation.get(field, "").strip(), (item.get("id"), field)
            assert item.get("reviewerNote", "").strip(), item.get("id")
            approved.append(item)
        else:
            assert item.get("selectionStatus") == "CONFIRMED", item.get("id")
            assert item.get("releaseBlockers"), item.get("id")
            pending.append(item)

    # The exact 99-name source artifact is imported as a separate content category.
    manifests = data.get("confirmedSelections", [])
    asma_manifest = [entry for entry in manifests if entry.get("id") == "asma-ul-husna-99"]
    assert len(asma_manifest) == 1 and asma_manifest[0].get("expectedCount") == 99
    assert asma_manifest[0].get("selectionStatus") == "CONFIRMED"

    asma_items = [item for item in items if item["category"] == "ASMA_UL_HUSNA"]
    assert len(asma_items) == 99, f"Expected 99 Asma records, got {len(asma_items)}"
    expected_asma_ids = {f"asma-{number:02d}" for number in range(1, 100)}
    assert {item["id"] for item in asma_items} == expected_asma_ids
    for item in asma_items:
        assert item.get("number") in range(1, 100), item.get("id")
        assert item.get("transliteration", "").strip(), item.get("id")
        assert {t.get("languageCode") for t in item.get("translations", [])} == REQUIRED_LANGUAGES, item.get("id")
        assert item.get("reviewStatus") == "IN_REVIEW", item.get("id")

    print(f"Catalogue OK: {len(items)} records; {len(approved)} release-ready; {len(pending)} confirmed but blocked; 99 Asma records present.")
    print("Hadith source allowlist OK: Sahih al-Bukhari and Sahih Muslim only.")


if __name__ == "__main__":
    main()
