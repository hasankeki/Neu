package app.nurwallpaper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class NurWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine = NurEngine()

    private inner class NurEngine : Engine() {
        private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(5, 9, 24)
        }
        private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 54f
        }
        private val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(0, 229, 255)
            textAlign = Paint.Align.CENTER
            textSize = 25f
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
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
                if (canvas != null) {
                    canvas.drawRect(
                        Rect(0, 0, canvas.width, canvas.height),
                        backgroundPaint
                    )
                    val centerX = canvas.width / 2f
                    val centerY = canvas.height / 2f
                    canvas.drawText("NurWallpaper", centerX, centerY, titlePaint)
                    canvas.drawText(
                        "Erst die Botschaft, dann die Effekte.",
                        centerX,
                        centerY + 52f,
                        subtitlePaint
                    )
                }
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
