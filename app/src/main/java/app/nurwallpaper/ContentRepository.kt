package app.nurwallpaper

/**
 * Typed content model for the offline-first library.
 *
 * Draft and research records may exist in the source tree, but only records
 * that pass the release gate are exposed by ContentRepository.
 */
enum class ContentCategory {
    VERSE,
    HADITH,
    DUA_DHIKR,
    ASMA_UL_HUSNA
}

enum class ContentReviewStatus {
    RESEARCH,
    DRAFT,
    IN_REVIEW,
    APPROVED_FOR_RELEASE
}

data class TranslationText(
    val languageCode: String,
    val text: String,
    val translator: String,
    val edition: String,
    val version: String,
    val license: String,
    val attribution: String
) {
    fun isReleaseReady(): Boolean =
        languageCode in setOf("de", "tr", "en") &&
            text.isNotBlank() &&
            translator.isNotBlank() &&
            edition.isNotBlank() &&
            version.isNotBlank() &&
            license.isNotBlank() &&
            attribution.isNotBlank()
}

data class ContentSource(
    val collection: String,
    val locator: String,
    val sourceUrl: String,
    val license: String,
    val attribution: String
) {
    fun isReleaseReady(): Boolean =
        collection.isNotBlank() &&
            locator.isNotBlank() &&
            sourceUrl.isNotBlank() &&
            license.isNotBlank() &&
            attribution.isNotBlank()
}

data class ContentItem(
    val id: String,
    val category: ContentCategory,
    val originalLanguageCode: String = "ar",
    val originalText: String,
    val translations: List<TranslationText>,
    val source: ContentSource,
    val reviewStatus: ContentReviewStatus,
    val reviewerNote: String? = null
) {
    fun isReleaseReady(): Boolean =
        id.isNotBlank() &&
            originalLanguageCode == "ar" &&
            originalText.isNotBlank() &&
            source.isReleaseReady() &&
            reviewStatus == ContentReviewStatus.APPROVED_FOR_RELEASE &&
            translations.all { it.isReleaseReady() } &&
            translations.map { it.languageCode }.toSet() == setOf("de", "tr", "en") &&
            reviewerNote?.isNotBlank() == true

    /**
     * Arabic is always shown from the verified original. Other languages
     * require an explicitly documented, release-ready translation.
     */
    fun textFor(languageCode: String): String? {
        if (!isReleaseReady()) return null
        if (languageCode == "ar") return originalText
        return translations.firstOrNull {
            it.languageCode == languageCode && it.isReleaseReady()
        }?.text
    }
}

class ContentRepository(
    private val records: List<ContentItem> = emptyList()
) {
    fun approved(category: ContentCategory? = null): List<ContentItem> =
        records.filter { item ->
            item.isReleaseReady() && (category == null || item.category == category)
        }

    fun findApproved(id: String): ContentItem? =
        records.firstOrNull { it.id == id && it.isReleaseReady() }

    fun displayText(id: String, languageCode: String): String? =
        findApproved(id)?.textFor(languageCode)
}
