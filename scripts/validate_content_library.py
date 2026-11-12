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

    # The traditional 99-name selection is recorded separately until its exact
    # approved source artifact can be recovered; it is not fabricated here.
    manifests = data.get("confirmedSelections", [])
    asma = [entry for entry in manifests if entry.get("id") == "asma-ul-husna-99"]
    assert len(asma) == 1 and asma[0].get("expectedCount") == 99
    assert asma[0].get("selectionStatus") == "CONFIRMED"

    print(f"Catalogue OK: {len(items)} records; {len(approved)} release-ready; {len(pending)} confirmed but blocked.")
    print("Hadith source allowlist OK: Sahih al-Bukhari and Sahih Muslim only.")


if __name__ == "__main__":
    main()
