package app.nurwallpaper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RotationScheduleTest {
    @Test
    fun oftenChangesEveryThirtyMinutes() {
        assertEquals(30L * 60L * 1000L, RotationSchedule.intervalMillis(RotationMode.OFTEN))
    }

    @Test
    fun rareChangesEveryThreeHours() {
        assertEquals(3L * 60L * 60L * 1000L, RotationSchedule.intervalMillis(RotationMode.RARE))
    }

    @Test
    fun favoritesOnlyUsesFrequentIntervalAndRestrictsItsPool() {
        assertEquals(
            RotationSchedule.OFTEN_INTERVAL_MILLIS,
            RotationSchedule.intervalMillis(RotationMode.FAVORITES_ONLY)
        )
    }

    @Test
    fun neverHasNoAutomaticInterval() {
        assertNull(RotationSchedule.intervalMillis(RotationMode.NEVER))
        assertNull(RotationSchedule.nextAt(10_000L, RotationMode.NEVER))
    }

    @Test
    fun nextDeadlineIsCalculatedFromCurrentTime() {
        assertEquals(
            1_900_000L,
            RotationSchedule.nextAt(100_000L, RotationMode.OFTEN)
        )
    }

    @Test
    fun dueCheckDoesNotRotateEarly() {
        assertFalse(RotationSchedule.isDue(99_999L, 100_000L))
        assertTrue(RotationSchedule.isDue(100_000L, 100_000L))
    }
}
