package app.nurwallpaper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ContentRepositoryTest {

    private fun approvedItem(
        status: ContentReviewStatus = ContentReviewStatus.APPROVED_FOR_RELEASE,
        translations: List<TranslationText> = listOf(
            translation("de"),
            translation("tr"),
            translation("en")
        ),
        reviewerNote: String? = "Reviewed against the cited source"
    ) = ContentItem(
        id = "test-verse-1",
        category = ContentCategory.VERSE,
        originalText = "نَصٌّ عَرَبِيٌّ",
        translations = translations,
        source = ContentSource(
            collection = "Test source",
            locator = "2:255",
            sourceUrl = "https://example.org/source",
            license = "Test license",
            attribution = "Test attribution"
        ),
        reviewStatus = status,
        reviewerNote = reviewerNote
    )

    private fun translation(language: String) = TranslationText(
        languageCode = language,
        text = "Verified $language translation",
        translator = "Named translator",
        edition = "Test edition",
        version = "1.0",
        license = "Test translation license",
        attribution = "Test attribution"
    )

    @Test
    fun completeHumanReviewedRecordPassesReleaseGate() {
        assertTrue(approvedItem().isReleaseReady())
    }

    @Test
    fun recordWithoutAllThreeTranslationsIsRejected() {
        assertFalse(approvedItem(translations = listOf(translation("de"), translation("tr"))).isReleaseReady())
    }

    @Test
    fun recordWithoutHumanReviewNoteIsRejected() {
        assertFalse(approvedItem(reviewerNote = null).isReleaseReady())
    }

    @Test
    fun recordNotApprovedForReleaseIsRejected() {
        assertFalse(approvedItem(status = ContentReviewStatus.IN_REVIEW).isReleaseReady())
    }

    @Test
    fun arabicUsesOriginalAndOtherLanguagesUseTheirOwnTranslation() {
        val item = approvedItem()
        assertEquals("نَصٌّ عَرَبِيٌّ", item.textFor("ar"))
        assertEquals("Verified de translation", item.textFor("de"))
        assertNull(item.textFor("fr"))
    }

    @Test
    fun arabicDisplayKeepsArabicAndUsesSelectedTranslation() {
        assertEquals("de", translationLanguageFor("ar", "de"))
        assertEquals("tr", translationLanguageFor("ar", "tr"))
        assertEquals("en", translationLanguageFor("ar", "en"))
    }

    @Test
    fun unsupportedDisplayLanguageFallsBackToGermanTranslation() {
        assertEquals("de", translationLanguageFor("ar", "fr"))
    }

    @Test
    fun normalDisplayLanguageRemainsTheTranslationLanguage() {
        assertEquals("tr", translationLanguageFor("tr", "de"))
    }

    @Test
    fun duplicateTranslationLanguageIsRejected() {
        assertFalse(
            approvedItem(
                translations = listOf(translation("de"), translation("de"), translation("en"))
            ).isReleaseReady()
        )
    }

    @Test
    fun hadithFromOutsideBukhariAndMuslimIsRejected() {
        val hadith = approvedItem().copy(
            category = ContentCategory.HADITH,
            source = approvedItem().source.copy(collection = "Sunan collection")
        )
        assertFalse(hadith.isReleaseReady())
    }

    @Test
    fun misleadingHadithCollectionNameIsRejected() {
        val hadith = approvedItem().copy(
            category = ContentCategory.HADITH,
            source = approvedItem().source.copy(collection = "Not Sahih al-Bukhari")
        )
        assertFalse(hadith.isReleaseReady())
    }

    @Test
    fun exactBukhariAndMuslimCollectionNamesAreAllowed() {
        val base = approvedItem()
        val bukhari = base.copy(
            category = ContentCategory.HADITH,
            source = base.source.copy(collection = "Ṣaḥīḥ al-Bukhārī")
        )
        val muslim = base.copy(
            category = ContentCategory.HADITH,
            source = base.source.copy(collection = "Sahih Muslim")
        )
        assertTrue(bukhari.isReleaseReady())
        assertTrue(muslim.isReleaseReady())
    }

    @Test
    fun repositoryNeverReturnsUnapprovedRecords() {
        val repository = ContentRepository(
            listOf(
                approvedItem(),
                approvedItem(status = ContentReviewStatus.DRAFT).copy(id = "draft")
            )
        )
        assertEquals(listOf("test-verse-1"), repository.approved().map { it.id })
    }
}
