package app.nurwallpaper

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationFreshnessTest {
    @Test
    fun acceptsCurrentAndRecentLocationFixes() {
        val now = 1_800_000_000_000L
        assertTrue(isLocationTimestampFresh(now, now))
        assertTrue(isLocationTimestampFresh(now - 60_000L, now))
        assertTrue(isLocationTimestampFresh(now - 6 * 60 * 60 * 1000L, now))
    }

    @Test
    fun rejectsLocationFixesOlderThanSixHours() {
        val now = 1_800_000_000_000L
        assertFalse(isLocationTimestampFresh(now - 6 * 60 * 60 * 1000L - 1L, now))
    }

    @Test
    fun rejectsFutureDatedLocationFixes() {
        val now = 1_800_000_000_000L
        assertFalse(isLocationTimestampFresh(now + 1L, now))
    }
}
