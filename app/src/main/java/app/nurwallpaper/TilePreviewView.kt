package app.nurwallpaper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.os.SystemClock
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

/**
 * Lightweight, procedural tile preview. No bitmap assets or network requests.
 * The moving highlight can be paused independently from the wallpaper service.
 */
class TilePreviewView(context: Context) : View(context) {
    private val tilePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = dp(1.1f)
    }
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val starPath = Path()
    private val startedAt = SystemClock.uptimeMillis()

    var isLightPaused: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    private fun dp(value: Float) = value * resources.displayMetrics.density

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0f || h <= 0f) return

        backgroundPaint.shader = LinearGradient(
            0f, 0f, w, h,
            intArrayOf(Color.rgb(4, 25, 27), Color.rgb(7, 54, 48), Color.rgb(3, 25, 31)),
            null, Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, 0f, w, h, backgroundPaint)
        backgroundPaint.shader = null

        val cell = dp(76f)
        val cols = (w / cell).toInt() + 2
        val rows = (h / cell).toInt() + 2
        val offsetX = (w - (cols - 1) * cell) / 2f
        val offsetY = (h - (rows - 1) * cell) / 2f
        val phase = if (isLightPaused) 0f else ((SystemClock.uptimeMillis() - startedAt) % 6200L) / 6200f

        for (row in -1 until rows) {
            for (col in -1 until cols) {
                val cx = offsetX + col * cell
                val cy = offsetY + row * cell
                drawTile(canvas, cx, cy, cell * 0.5f, phase, col, row)
            }
        }

        // A soft moving band crosses the engraved geometry, not the whole screen.
        if (!isLightPaused) {
            val sweepX = -w * 0.35f + (w * 1.7f) * phase
            glowPaint.shader = LinearGradient(
                sweepX - dp(34f), 0f, sweepX + dp(34f), 0f,
                intArrayOf(0x00000000, 0x665FE8B5, 0xB8D9F3C5.toInt(), 0x665FE8B5, 0x00000000),
                floatArrayOf(0f, .32f, .5f, .68f, 1f), Shader.TileMode.CLAMP
            )
            canvas.drawRect(0f, 0f, w, h, glowPaint)
            glowPaint.shader = null
            postInvalidateDelayed(33L)
        }
    }

    private fun drawTile(canvas: Canvas, cx: Float, cy: Float, r: Float, phase: Float, col: Int, row: Int) {
        val rect = RectF(cx - r, cy - r, cx + r, cy + r)
        tilePaint.shader = LinearGradient(
            rect.left, rect.top, rect.right, rect.bottom,
            intArrayOf(0xFF174D43.toInt(), 0xFF0B332F.toInt(), 0xFF082923.toInt()),
            null, Shader.TileMode.CLAMP
        )
        canvas.drawRoundRect(rect, dp(4f), dp(4f), tilePaint)
        tilePaint.shader = null

        linePaint.color = 0xA5C7A66A.toInt()
        linePaint.strokeWidth = dp(1f)
        canvas.drawRoundRect(rect, dp(4f), dp(4f), linePaint)

        // Interlocking eight-point geometry gives the tiles a carved, architectural feel.
        starPath.reset()
        for (i in 0 until 16) {
            val angle = Math.PI * i / 8.0 - Math.PI / 2.0
            val radius = if (i % 2 == 0) r * .72f else r * .43f
            val x = cx + cos(angle).toFloat() * radius
            val y = cy + sin(angle).toFloat() * radius
            if (i == 0) starPath.moveTo(x, y) else starPath.lineTo(x, y)
        }
        starPath.close()
        linePaint.color = 0xC8D0B477.toInt()
        linePaint.strokeWidth = dp(1.15f)
        canvas.drawPath(starPath, linePaint)

        linePaint.color = 0x668BE0B9.toInt()
        linePaint.strokeWidth = dp(.8f)
        val inner = r * .28f
        canvas.drawCircle(cx, cy, inner, linePaint)
        canvas.drawLine(cx - inner, cy, cx + inner, cy, linePaint)
        canvas.drawLine(cx, cy - inner, cx, cy + inner, linePaint)

        if (!isLightPaused) {
            val local = ((phase + (col * .13f) + (row * .07f)) % 1f)
            val shimmer = (0.5 + 0.5 * sin(local * Math.PI * 2)).toFloat()
            glowPaint.color = Color.argb((18 + shimmer * 45).toInt(), 177, 235, 194)
            glowPaint.style = Paint.Style.STROKE
            glowPaint.strokeWidth = dp(2f)
            canvas.drawCircle(cx, cy, r * (.18f + shimmer * .05f), glowPaint)
            glowPaint.style = Paint.Style.FILL
        }
    }
}
