package app.nurwallpaper

import android.content.Context
import org.json.JSONObject

/**
 * Reads the bundled, offline-first content catalogue.
 * Invalid records are ignored by the strict ContentItem release gate.
 */
object ContentLibraryLoader {
    fun load(context: Context): ContentRepository {
        val json = context.assets.open("content-library.json")
            .bufferedReader(Charsets.UTF_8)
            .use { it.readText() }
        val root = JSONObject(json)
        val array = root.optJSONArray("items") ?: return ContentRepository()
        val records = buildList {
            for (index in 0 until array.length()) {
                val raw = array.optJSONObject(index) ?: continue
                runCatching { raw.toContentItem() }
                    .getOrNull()
                    ?.let(::add)
            }
        }
        return ContentRepository(records)
    }

    private fun JSONObject.toContentItem(): ContentItem {
        val sourceJson = getJSONObject("source")
        val translationsJson = getJSONArray("translations")
        val translations = buildList {
            for (index in 0 until translationsJson.length()) {
                val item = translationsJson.getJSONObject(index)
                add(
                    TranslationText(
                        languageCode = item.getString("languageCode"),
                        text = item.getString("text"),
                        translator = item.getString("translator"),
                        edition = item.getString("edition"),
                        version = item.getString("version"),
                        license = item.getString("license"),
                        attribution = item.getString("attribution")
                    )
                )
            }
        }
        return ContentItem(
            id = getString("id"),
            category = ContentCategory.valueOf(getString("category")),
            originalLanguageCode = optString("originalLanguageCode", "ar"),
            originalText = getString("originalText"),
            translations = translations,
            source = ContentSource(
                collection = sourceJson.getString("collection"),
                locator = sourceJson.getString("locator"),
                sourceUrl = sourceJson.getString("sourceUrl"),
                license = sourceJson.getString("license"),
                attribution = sourceJson.getString("attribution")
            ),
            reviewStatus = ContentReviewStatus.valueOf(getString("reviewStatus")),
            reviewerNote = optString("reviewerNote").takeIf { it.isNotBlank() }
        )
    }
}
