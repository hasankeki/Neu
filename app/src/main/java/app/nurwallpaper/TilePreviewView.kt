package app.nurwallpaper

import android.content.Context
import android.os.SystemClock
import android.view.View

/**
 * Preview uses exactly the same procedural renderer as NurWallpaperService.
 * Pausing freezes the highlight phase without changing the underlying design.
 */
class TilePreviewView(context: Context) : View(context) {
    private val renderer = PrestigeTileRenderer()
    private val startedAt = SystemClock.uptimeMillis()
    private var frozenPhase = 0f

    var isLightPaused: Boolean = false
        set(value) {
            if (value && !field) frozenPhase = currentPhase()
            field = value
            invalidate()
        }

    private fun currentPhase(): Float =
        ((SystemClock.uptimeMillis() - startedAt) % 9000L) / 9000f

    override fun onDraw(canvas: android.graphics.Canvas) {
        super.onDraw(canvas)
        val phase = if (isLightPaused) frozenPhase else currentPhase()
        renderer.draw(canvas, phase, animated = !isLightPaused)
        if (!isLightPaused) postInvalidateDelayed(33L)
    }
}
