package app.nurwallpaper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import kotlin.random.Random

class NurWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine = NurEngine()

    private inner class NurEngine : Engine() {
        private val skyPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val mountainPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val ridgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(36, 102, 126)
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        private val starsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(205, 230, 245)
        }
        private val panelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 54f
            setShadowLayer(8f, 0f, 2f, Color.BLACK)
        }
        private val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(180, 225, 232)
            textAlign = Paint.Align.CENTER
            textSize = 25f
            setShadowLayer(5f, 0f, 1f, Color.BLACK)
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            drawFrame()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) drawFrame()
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) drawScene(canvas)
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas)
            }
        }

        private fun drawScene(canvas: Canvas) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            skyPaint.shader = LinearGradient(
                0f, 0f, 0f, h,
                Color.rgb(7, 13, 35),
                Color.rgb(19, 35, 61),
                Shader.TileMode.CLAMP
            )
            canvas.drawRect(0f, 0f, w, h, skyPaint)
            skyPaint.shader = null

            val random = Random(19)
            repeat(85) {
                val x = random.nextFloat() * w
                val y = random.nextFloat() * h * 0.62f
                val radius = 0.7f + random.nextFloat() * 1.5f
                canvas.drawCircle(x, y, radius, starsPaint)
            }

            drawMountain(canvas, w, h, 0.67f, Color.rgb(18, 35, 59), 0.12f)
            drawMountain(canvas, w, h, 0.76f, Color.rgb(11, 25, 47), 0.19f)
            drawMountain(canvas, w, h, 0.86f, Color.rgb(5, 15, 32), 0.25f)

            val centerX = w / 2f
            val centerY = h * 0.48f
            val panel = RectF(w * 0.07f, centerY - 92f, w * 0.93f, centerY + 94f)
            panelPaint.shader = LinearGradient(
                panel.left, panel.top, panel.right, panel.bottom,
                intArrayOf(0x00202B3C, 0xB8000818.toInt(), 0x00202B3C),
                null,
                Shader.TileMode.CLAMP
            )
            canvas.drawRoundRect(panel, 34f, 34f, panelPaint)
            panelPaint.shader = null

            canvas.drawText("NurWallpaper", centerX, centerY - 5f, titlePaint)
            canvas.drawText(
                "Erst die Botschaft, dann die Effekte.",
                centerX, centerY + 43f, subtitlePaint
            )
        }

        private fun drawMountain(canvas: Canvas, width: Float, height: Float, base: Float, color: Int, variation: Float) {
            val path = Path().apply {
                moveTo(0f, height * base)
                lineTo(width * 0.18f, height * (base - variation * 0.65f))
                lineTo(width * 0.34f, height * (base - variation * 0.25f))
                lineTo(width * 0.57f, height * (base - variation))
                lineTo(width * 0.76f, height * (base - variation * 0.35f))
                lineTo(width, height * (base - variation * 0.72f))
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            mountainPaint.color = color
            canvas.drawPath(path, mountainPaint)
            canvas.drawPath(path, ridgePaint)
        }
    }
}
