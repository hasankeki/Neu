package app.nurwallpaper

/**
 * Fixed Free-tier intervals. PRO may later expose a user-controlled interval.
 * FAVORITES_ONLY is a content-pool restriction and uses the frequent default.
 */
object RotationSchedule {
    const val OFTEN_INTERVAL_MILLIS: Long = 30L * 60L * 1000L
    const val RARE_INTERVAL_MILLIS: Long = 3L * 60L * 60L * 1000L

    fun intervalMillis(mode: RotationMode): Long? = when (mode) {
        RotationMode.NEVER -> null
        RotationMode.RARE -> RARE_INTERVAL_MILLIS
        RotationMode.OFTEN, RotationMode.FAVORITES_ONLY -> OFTEN_INTERVAL_MILLIS
    }

    fun nextAt(nowMillis: Long, mode: RotationMode): Long? =
        intervalMillis(mode)?.let { nowMillis + it }

    fun isDue(nowMillis: Long, nextAtMillis: Long): Boolean =
        nextAtMillis <= 0L || nowMillis >= nextAtMillis
}
