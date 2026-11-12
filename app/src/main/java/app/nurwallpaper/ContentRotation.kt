package app.nurwallpaper

import android.content.SharedPreferences

enum class RotationMode {
    NEVER,
    RARE,
    OFTEN,
    FAVORITES_ONLY;

    companion object {
        fun fromSetting(value: String?): RotationMode = when (value) {
            "Selten" -> RARE
            "Oft" -> OFTEN
            "Nur Favoriten" -> FAVORITES_ONLY
            else -> NEVER
        }
    }
}

/** Persists the user's favorites independently from the content records. */
class FavoriteStore(private val preferences: SharedPreferences) {
    fun all(): Set<String> =
        preferences.getStringSet(KEY_FAVORITES, emptySet())?.toSet() ?: emptySet()

    fun contains(contentId: String): Boolean = contentId in all()

    fun setFavorite(contentId: String, favorite: Boolean) {
        if (contentId.isBlank()) return
        val updated = all().toMutableSet()
        if (favorite) updated.add(contentId) else updated.remove(contentId)
        preferences.edit().putStringSet(KEY_FAVORITES, updated).apply()
    }

    fun toggle(contentId: String): Boolean {
        val nowFavorite = !contains(contentId)
        setFavorite(contentId, nowFavorite)
        return nowFavorite
    }

    private companion object {
        const val KEY_FAVORITES = "favorite_content_ids"
    }
}

/**
 * Selects the next approved item without repeating an item before the active
 * pool has been exhausted. The caller decides when RARE/OFTEN intervals elapse.
 */
class ContentRotationManager(private val preferences: SharedPreferences) {

    fun next(
        items: List<ContentItem>,
        mode: RotationMode,
        favoriteIds: Set<String>,
        currentId: String? = null
    ): ContentItem? {
        val approved = items.filter { it.isReleaseReady() }
        if (mode == RotationMode.NEVER) {
            return approved.firstOrNull { it.id == currentId }
        }

        val pool = if (mode == RotationMode.FAVORITES_ONLY) {
            approved.filter { it.id in favoriteIds }
        } else {
            approved
        }
        if (pool.isEmpty()) return null

        val poolKey = pool.map { it.id }.sorted().joinToString("|")
        val poolScope = if (mode == RotationMode.FAVORITES_ONLY) "favorites" else "all"
        val seenKey = "rotation_seen_${poolScope}_$poolKey"
        val seen = preferences.getStringSet(seenKey, emptySet())?.toMutableSet()
            ?: mutableSetOf()

        // Count the currently displayed item as seen before choosing the next.
        if (currentId != null && pool.any { it.id == currentId }) {
            seen.add(currentId)
        }

        var unseen = pool.filterNot { it.id in seen }
        if (unseen.isEmpty()) {
            seen.clear()
            unseen = pool.filterNot { it.id == currentId }
            if (unseen.isEmpty()) unseen = pool
        }

        val selected = unseen.random()
        seen.add(selected.id)
        preferences.edit().putStringSet(seenKey, seen).apply()
        return selected
    }
}
