package app.nurwallpaper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RectF
import android.graphics.Shader
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Continuous Islamic geometric ornament field. Preview and live wallpaper share
 * this renderer so the geometry and edge-light animation stay identical.
 */
internal class PrestigeTileRenderer {
    private val bg = Paint(Paint.ANTI_ALIAS_FLAG)
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val line = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    private val halo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    private val core = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    private val path = Path()
    private val pulsePath = Path()

    fun draw(canvas: Canvas, phase: Float, animated: Boolean) {
        val w = canvas.width.toFloat()
        val h = canvas.height.toFloat()
        if (w <= 0f || h <= 0f) return
        val unit = min(w, h)
        bg.shader = LinearGradient(0f, 0f, w, h,
            intArrayOf(0xFF031512.toInt(), 0xFF064334.toInt(), 0xFF021D1B.toInt()),
            null, Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, w, h, bg)
        bg.shader = null

        // Deep translucent emerald panels are continuous; no isolated square tiles.
        val spacing = unit * .53f
        val radius = spacing * .47f
        val cols = (w / spacing).toInt() + 4
        val rows = (h / spacing).toInt() + 4
        val ox = (w - (cols - 1) * spacing) * .5f
        val oy = (h - (rows - 1) * spacing) * .5f

        for (row in -1 until rows) for (col in -1 until cols) {
            val cx = ox + col * spacing
            val cy = oy + row * spacing
            drawStarPanel(canvas, cx, cy, radius)
        }

        // Connect neighboring stars with shared diamond/hexagon seams.
        line.strokeWidth = unit * .0032f
        line.color = 0xB8D2AD70.toInt()
        for (row in -1 until rows) for (col in -1 until cols) {
            val cx = ox + col * spacing
            val cy = oy + row * spacing
            val half = spacing * .5f
            // Fine connecting diagonals form a single interlocking tessellation.
            canvas.drawLine(cx, cy - radius * .47f, cx + half, cy - half, line)
            canvas.drawLine(cx + half, cy - half, cx + spacing, cy, line)
            canvas.drawLine(cx + spacing, cy, cx + half, cy + half, line)
            canvas.drawLine(cx + half, cy + half, cx, cy + radius * .47f, line)
        }

        // Moving warm light runs along the actual ornament edges, not across the screen.
        if (animated) {
            for (row in -1 until rows) for (col in -1 until cols) {
                val cx = ox + col * spacing
                val cy = oy + row * spacing
                val local = ((phase + col * .071f + row * .047f) % 1f + 1f) % 1f
                drawEdgePulse(canvas, cx, cy, radius, local, unit)
            }
        }
    }

    private fun drawStarPanel(canvas: Canvas, cx: Float, cy: Float, r: Float) {
        // Faceted emerald ground, softly shaded rather than a raised square tile.
        val bounds = RectF(cx-r, cy-r, cx+r, cy+r)
        fill.shader = LinearGradient(bounds.left, bounds.top, bounds.right, bounds.bottom,
            intArrayOf(0xCC17644F.toInt(), 0xCC073B32.toInt(), 0xCC032722.toInt()),
            null, Shader.TileMode.CLAMP)
        path.reset()
        starPath(path, cx, cy, r * .98f, r * .60f)
        canvas.drawPath(path, fill)
        fill.shader = null

        // Dark engraved edge and warm metallic bevel.
        line.strokeWidth = r * .055f
        line.color = 0xAA001A16.toInt()
        canvas.drawPath(path, line)
        line.strokeWidth = r * .022f
        line.color = 0xD7D8B77A.toInt()
        canvas.drawPath(path, line)

        // Inner floral geometry gives the star a crafted, layered center.
        line.strokeWidth = r * .012f
        line.color = 0xA9A9D5B0.toInt()
        val inner = Path()
        starPath(inner, cx, cy, r * .52f, r * .31f)
        canvas.drawPath(inner, line)
        canvas.drawCircle(cx, cy, r * .115f, line)
        canvas.drawCircle(cx, cy, r * .075f, line)
        for (i in 0 until 8) {
            val a = Math.PI * i / 4.0
            val x = cx + cos(a).toFloat() * r * .23f
            val y = cy + sin(a).toFloat() * r * .23f
            canvas.drawCircle(x, y, r * .055f, line)
        }
    }

    private fun starPath(out: Path, cx: Float, cy: Float, outer: Float, inner: Float) {
        out.reset()
        for (i in 0 until 16) {
            val a = Math.PI * i / 8.0 - Math.PI / 2.0
            val rr = if (i % 2 == 0) outer else inner
            val x = cx + cos(a).toFloat() * rr
            val y = cy + sin(a).toFloat() * rr
            if (i == 0) out.moveTo(x, y) else out.lineTo(x, y)
        }
        out.close()
    }

    private fun drawEdgePulse(canvas: Canvas, cx: Float, cy: Float, r: Float,
                              phase: Float, unit: Float) {
        path.reset()
        starPath(path, cx, cy, r * .98f, r * .60f)
        val measure = PathMeasure(path, true)
        val length = measure.length
        if (length <= 0f) return
        val pulseLength = length * .19f
        val start = phase * length
        val end = start + pulseLength
        pulsePath.reset()
        if (end <= length) {
            measure.getSegment(start, end, pulsePath, true)
        } else {
            measure.getSegment(start, length, pulsePath, true)
            measure.getSegment(0f, end - length, pulsePath, true)
        }

        // Broad translucent jade-gold bloom under a fine champagne-gold highlight.
        halo.color = 0x7A50D6A4
        halo.strokeWidth = unit * .025f
        halo.setShadowLayer(unit * .018f, 0f, 0f, 0xCC38D49A.toInt())
        canvas.drawPath(pulsePath, halo)
        halo.clearShadowLayer()

        core.color = 0xFFFFD98A.toInt()
        core.strokeWidth = unit * .0045f
        canvas.drawPath(pulsePath, core)
    }
}
