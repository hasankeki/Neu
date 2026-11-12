package app.nurwallpaper

/** Rejects stale and future-dated location fixes before using them for prayer times or Qibla. */
internal fun isLocationTimestampFresh(
    locationTimeMillis: Long,
    nowMillis: Long,
    maxAgeMillis: Long = 6 * 60 * 60 * 1000L
): Boolean {
    val ageMillis = nowMillis - locationTimeMillis
    return ageMillis in 0..maxAgeMillis
}
