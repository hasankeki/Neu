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
 * One procedural source of truth for the app preview and the actual live wallpaper.
 * The geometry, palette, bevels and travelling highlights are deliberately shared.
 */
internal class PrestigeTileRenderer {
    private val background = Paint(Paint.ANTI_ALIAS_FLAG)
    private val tile = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bevel = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    private val ornament = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE }
    private val glow = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeCap = Paint.Cap.ROUND; strokeJoin = Paint.Join.ROUND }
    private val path = Path()
    private val lightPath = Path()

    fun draw(canvas: Canvas, phase: Float, animated: Boolean) {
        val w = canvas.width.toFloat()
        val h = canvas.height.toFloat()
        if (w <= 0f || h <= 0f) return
        val unit = min(w, h)
        background.shader = LinearGradient(0f, 0f, w, h,
            intArrayOf(0xFF031715.toInt(), 0xFF073B32.toInt(), 0xFF021C1D.toInt()),
            floatArrayOf(0f, .52f, 1f), Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, w, h, background)
        background.shader = null

        // Quiet radial depth behind the tessellation.
        background.shader = LinearGradient(w * .5f, 0f, w * .5f, h,
            intArrayOf(0x3326A982, 0x00101B18, 0x55000000), null, Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, w, h, background)
        background.shader = null

        val cell = unit * .235f
        val radius = cell * .49f
        val cols = (w / cell).toInt() + 3
        val rows = (h / cell).toInt() + 3
        val startX = (w - (cols - 1) * cell) / 2f
        val startY = (h - (rows - 1) * cell) / 2f
        for (row in -1 until rows) for (col in -1 until cols) {
            val cx = startX + col * cell
            val cy = startY + row * cell
            drawTile(canvas, cx, cy, radius, phase, col, row, animated)
        }
    }

    private fun drawTile(canvas: Canvas, cx: Float, cy: Float, r: Float,
                         phase: Float, col: Int, row: Int, animated: Boolean) {
        val rect = RectF(cx-r, cy-r, cx+r, cy+r)
        tile.shader = LinearGradient(rect.left, rect.top, rect.right, rect.bottom,
            intArrayOf(0xFF286B59.toInt(), 0xFF0D4439.toInt(), 0xFF062A27.toInt()),
            floatArrayOf(0f, .48f, 1f), Shader.TileMode.CLAMP)
        canvas.drawRoundRect(rect, r*.10f, r*.10f, tile)
        tile.shader = null

        // A restrained top-left bevel and dark lower edge create physical depth.
        bevel.strokeWidth = r*.055f
        bevel.color = 0x887DE0B2.toInt()
        path.reset()
        path.moveTo(rect.left+r*.08f, rect.bottom-r*.08f)
        path.lineTo(rect.left+r*.08f, rect.top+r*.10f)
        path.lineTo(rect.right-r*.10f, rect.top+r*.10f)
        canvas.drawPath(path, bevel)
        bevel.color = 0xAA001713.toInt()
        path.reset()
        path.moveTo(rect.right-r*.08f, rect.top+r*.10f)
        path.lineTo(rect.right-r*.08f, rect.bottom-r*.08f)
        path.lineTo(rect.left+r*.08f, rect.bottom-r*.08f)
        canvas.drawPath(path, bevel)

        // Interlocking eight-point star with a second engraved contour.
        ornament.strokeWidth = r*.025f
        ornament.color = 0xD9D7B77A.toInt()
        drawStar(canvas, cx, cy, r*.79f, r*.49f)
        ornament.strokeWidth = r*.012f
        ornament.color = 0x9988D8B4.toInt()
        drawStar(canvas, cx, cy, r*.61f, r*.38f)
        ornament.color = 0xB9D5B77D.toInt()
        canvas.drawCircle(cx, cy, r*.19f, ornament)
        canvas.drawCircle(cx, cy, r*.115f, ornament)
        // Fine diagonal connectors make the repeated tiles read as one tessellation.
        ornament.color = 0x668AD0AD.toInt()
        ornament.strokeWidth = r*.010f
        for (i in 0 until 4) {
            val a = Math.PI * (i * 2 + 1) / 4.0
            val x = cx + cos(a).toFloat()*r*.49f
            val y = cy + sin(a).toFloat()*r*.49f
            canvas.drawLine(cx, cy, x, y, ornament)
        }

        if (animated) {
            // A short amber/mint light pulse travels on the engraved star line.
            // Phase offsets make it ripple through the tessellation, not flash flat.
            val local = ((phase + col * .083f + row * .057f) % 1f + 1f) % 1f
            drawLightPulse(canvas, cx, cy, r, local)
        }
    }

    private fun drawStar(canvas: Canvas, cx: Float, cy: Float, outer: Float, inner: Float) {
        path.reset()
        for (i in 0 until 16) {
            val a = Math.PI*i/8.0 - Math.PI/2.0
            val radius = if (i%2==0) outer else inner
            val x = cx + cos(a).toFloat()*radius
            val y = cy + sin(a).toFloat()*radius
            if (i==0) path.moveTo(x,y) else path.lineTo(x,y)
        }
        path.close()
        canvas.drawPath(path, ornament)
    }

    /**
     * Draw a short moving arc along each star contour. The previous full-screen
     * vertical shader lit every star at once and looked like a white pattern.
     */
    private fun drawLightPulse(canvas: Canvas, cx: Float, cy: Float, r: Float, phase: Float) {
        path.reset()
        for (i in 0 until 16) {
            val a = Math.PI * i / 8.0 - Math.PI / 2.0
            val radius = if (i % 2 == 0) r * .79f else r * .49f
            val px = cx + cos(a).toFloat() * radius
            val py = cy + sin(a).toFloat() * radius
            if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
        }
        path.close()

        val measure = PathMeasure(path, true)
        val length = measure.length
        if (length <= 0f) return
        val pulseLength = length * .16f
        val distance = phase * length
        lightPath.reset()
        val end = distance + pulseLength
        if (end <= length) {
            measure.getSegment(distance, end, lightPath, true)
        } else {
            measure.getSegment(distance, length, lightPath, true)
            measure.getSegment(0f, end - length, lightPath, true)
        }

        // Soft green-gold halo, then a fine gold core: no stark white outline.
        glow.shader = null
        glow.color = 0x6686E6B8
        glow.strokeWidth = r * .105f
        glow.setShadowLayer(r * .11f, 0f, 0f, 0xAA42C99A.toInt())
        canvas.drawPath(lightPath, glow)
        glow.clearShadowLayer()
        glow.color = 0xFFE3C27D.toInt()
        glow.strokeWidth = r * .026f
        canvas.drawPath(lightPath, glow)
    }
}
