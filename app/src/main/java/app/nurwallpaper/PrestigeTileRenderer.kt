package app.nurwallpaper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
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
        if (animated) drawTravellingLight(canvas, w, h, phase, unit)
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
            val local = ((phase + col*.071f + row*.043f) % 1f + 1f) % 1f
            val pulse = (.5 + .5*sin(local*Math.PI*2)).toFloat()
            glow.color = Color.argb((35 + pulse*105).toInt(), 185, 245, 207)
            glow.strokeWidth = r*(.025f + pulse*.018f)
            canvas.drawCircle(cx, cy, r*(.23f+pulse*.025f), glow)
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

    /** A narrow warm-white highlight travels through the engraved star contours. */
    private fun drawTravellingLight(canvas: Canvas, w: Float, h: Float, phase: Float, unit: Float) {
        val x = -unit*.25f + (w+unit*.5f)*phase
        glow.shader = LinearGradient(x-unit*.12f, 0f, x+unit*.12f, 0f,
            intArrayOf(0x00D8F2C8, 0x558BE6B8, 0xDDF7E8C4.toInt(), 0x558BE6B8, 0x00D8F2C8),
            floatArrayOf(0f,.34f,.5f,.66f,1f), Shader.TileMode.CLAMP)
        glow.strokeWidth = unit*.0045f
        // Clip the moving light to the tile field so it does not become a flat overlay.
        canvas.save()
        canvas.clipRect(0f, 0f, w, h)
        val cell = unit*.235f
        val cols = (w/cell).toInt()+3
        val rows = (h/cell).toInt()+3
        val sx = (w-(cols-1)*cell)/2f
        val sy = (h-(rows-1)*cell)/2f
        for (row in -1 until rows) for (col in -1 until cols) {
            val cx=sx+col*cell; val cy=sy+row*cell; val r=cell*.49f
            drawStar(canvas,cx,cy,r*.79f,r*.49f)
        }
        canvas.restore()
        glow.shader = null
    }
}
