package app.nurwallpaper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import kotlin.random.Random

private const val KEY_WALLPAPER_CONTENT_ID = "wallpaper_content_id"
private const val KEY_NEXT_ROTATION_AT = "wallpaper_next_rotation_at"

class NurWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine = NurEngine()

    private inner class NurEngine : Engine() {
        private val preferences by lazy {
            getSharedPreferences("nurwallpaper_settings", MODE_PRIVATE)
        }
        private val rotationHandler = Handler(Looper.getMainLooper())
        private val prestigeRenderer = PrestigeTileRenderer()
        private val animationTick = object : Runnable {
            override fun run() {
                if (isVisible && preferences.getBoolean("light_effect", true)) {
                    drawFrame()
                    rotationHandler.postDelayed(this, 33L)
                }
            }
        }
        private val rotationTick = object : Runnable {
            override fun run() {
                if (isVisible) {
                    updateRotationIfDue()
                    drawFrame()
                    scheduleNextRotation()
                }
            }
        }
        private val settingsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "rotation" || key == "favorite_content_ids") {
                rotationHandler.removeCallbacks(rotationTick)
                updateRotationIfDue(resetDeadline = key == "rotation")
                scheduleNextRotation()
            }
            if (key == "language" || key == "light_effect" || key == "rotation" || key == "favorite_content_ids") drawFrame()
            if (key == "light_effect") scheduleAnimation()
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            preferences.registerOnSharedPreferenceChangeListener(settingsListener)
        }

        override fun onDestroy() {
            rotationHandler.removeCallbacks(rotationTick)
            rotationHandler.removeCallbacks(animationTick)
            preferences.unregisterOnSharedPreferenceChangeListener(settingsListener)
            super.onDestroy()
        }

        private val contentRepository by lazy { runCatching { ContentLibraryLoader.load(this@NurWallpaperService) }.getOrElse { ContentRepository() } }
        private val contentPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 38f
            textAlign = Paint.Align.CENTER
            setShadowLayer(5f, 0f, 2f, Color.BLACK)
        }
        private val translationPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(224, 232, 242)
            textSize = 24f
            textAlign = Paint.Align.CENTER
            setShadowLayer(4f, 0f, 1f, Color.BLACK)
        }
        private val sourcePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(150, 190, 205)
            textSize = 17f
            textAlign = Paint.Align.CENTER
        }
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
            if (visible) {
                updateRotationIfDue()
                drawFrame()
                scheduleNextRotation()
                scheduleAnimation()
            } else {
                rotationHandler.removeCallbacks(rotationTick)
                rotationHandler.removeCallbacks(animationTick)
            }
        }

        /**
         * Rotation is driven by elapsed wall-clock deadlines, not by screen unlocks.
         * While the wallpaper is not visible no callbacks run; on return at most one
         * due change is applied, then a fresh interval starts.
         */
        private fun updateRotationIfDue(resetDeadline: Boolean = false) {
            val mode = RotationMode.fromSetting(preferences.getString("rotation", "Nie"))
            val interval = RotationSchedule.intervalMillis(mode) ?: run {
                rotationHandler.removeCallbacks(rotationTick)
                return
            }
            val now = System.currentTimeMillis()
            val nextAt = preferences.getLong(KEY_NEXT_ROTATION_AT, 0L)
            val items = contentRepository.approved()
            val favorites = FavoriteStore(preferences).all()
            val currentId = preferences.getString(KEY_WALLPAPER_CONTENT_ID, null)
            val currentIsEligible = items.any { item ->
                item.id == currentId && (mode != RotationMode.FAVORITES_ONLY || item.id in favorites)
            }
            val due = RotationSchedule.isDue(now, nextAt)

            val shouldRotate = !currentIsEligible || (due && !resetDeadline)
            if (shouldRotate) {
                val selected = ContentRotationManager(preferences).next(items, mode, favorites, currentId)
                val editor = preferences.edit()
                if (selected != null) editor.putString(KEY_WALLPAPER_CONTENT_ID, selected.id)
                else editor.remove(KEY_WALLPAPER_CONTENT_ID)
                editor.apply()
            }

            // Preserve an existing deadline on ordinary unlock/visibility changes.
            // A mode change, a completed interval, or a missing deadline starts a
            // fresh interval.
            if (resetDeadline || shouldRotate || nextAt <= 0L) {
                preferences.edit()
                    .putLong(KEY_NEXT_ROTATION_AT, now + interval)
                    .apply()
            }
        }

        private fun scheduleNextRotation() {
            rotationHandler.removeCallbacks(rotationTick)
            val mode = RotationMode.fromSetting(preferences.getString("rotation", "Nie"))
            val interval = RotationSchedule.intervalMillis(mode) ?: return
            if (!isVisible) return

            val now = System.currentTimeMillis()
            var nextAt = preferences.getLong(KEY_NEXT_ROTATION_AT, 0L)
            if (nextAt <= 0L) {
                nextAt = now + interval
                preferences.edit().putLong(KEY_NEXT_ROTATION_AT, nextAt).apply()
            }
            rotationHandler.postDelayed(rotationTick, (nextAt - now).coerceAtLeast(0L))
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
            val lightEnabled = preferences.getBoolean("light_effect", true)
            val phase = (SystemClock.uptimeMillis() % 9000L) / 9000f
            prestigeRenderer.draw(canvas, phase, animated = lightEnabled)

            val centerX = w / 2f
            val centerY = h * 0.49f
            val firstApproved = selectedWallpaperContent()
            if (firstApproved == null) {
                drawTextPanel(canvas, centerX, centerY, w)
                canvas.drawText("NurWallpaper", centerX, centerY - 5f, titlePaint)
                val language = preferences.getString("language", "de") ?: "de"
                val tagline = when (language) {
                    "tr" -> "Önce mesaj, sonra efektler."
                    "en" -> "The message comes before the effects."
                    "ar" -> "الرسالة أولاً، ثم المؤثرات."
                    else -> "Erst die Botschaft, dann die Effekte."
                }
                canvas.drawText(tagline, centerX, centerY + 43f, subtitlePaint)
            } else {
                val width = (w * 0.82f).toInt().coerceAtLeast(1)
                val language = preferences.getString("language", "de") ?: "de"
                val arabic = makeLayout(firstApproved.originalText, contentPaint, width, Layout.Alignment.ALIGN_CENTER)
                val translation = firstApproved.textFor(language)?.let {
                    makeLayout(it, translationPaint, width, Layout.Alignment.ALIGN_CENTER)
                }
                val sourceText = "${firstApproved.source.collection} · ${firstApproved.source.locator}"
                val sourceLayout = makeLayout(sourceText, sourcePaintAsText(), width, Layout.Alignment.ALIGN_CENTER)
                val totalHeight = arabic.height + (translation?.height ?: 0) + sourceLayout.height + 54f
                val top = (centerY - totalHeight / 2f).coerceAtLeast(24f)
                drawTextPanel(canvas, centerX, top + totalHeight / 2f, w, totalHeight + 34f)

                canvas.save()
                canvas.translate((w - width) / 2f, top)
                arabic.draw(canvas)
                canvas.restore()
                var nextY = top + arabic.height + 18f
                if (translation != null) {
                    canvas.save()
                    canvas.translate((w - width) / 2f, nextY)
                    translation.draw(canvas)
                    canvas.restore()
                    nextY += translation.height + 12f
                }
                canvas.save()
                canvas.translate((w - width) / 2f, nextY)
                sourceLayout.draw(canvas)
                canvas.restore()
            }
        }

        private fun drawTextPanel(canvas: Canvas, centerX: Float, centerY: Float, width: Float, height: Float = 180f) {
            val panel = RectF(width * .055f, centerY - height/2f, width * .945f, centerY + height/2f)
            panelPaint.shader = LinearGradient(panel.left, panel.top, panel.right, panel.bottom,
                intArrayOf(0x00101E19, 0xD9001713.toInt(), 0x00101E19), null, Shader.TileMode.CLAMP)
            canvas.drawRoundRect(panel, 28f, 28f, panelPaint)
            panelPaint.shader = null
        }

        private fun sourcePaintAsText(): TextPaint = TextPaint(sourcePaint).apply {
            textAlign = Paint.Align.CENTER
            textSize = 17f
        }

        private fun scheduleAnimation() {
            rotationHandler.removeCallbacks(animationTick)
            if (isVisible && preferences.getBoolean("light_effect", true)) {
                rotationHandler.postDelayed(animationTick, 33L)
            }
        }

        private fun selectedWallpaperContent(): ContentItem? {
            val items = contentRepository.approved()
            if (items.isEmpty()) return null

            val mode = RotationMode.fromSetting(preferences.getString("rotation", "Nie"))
            val favorites = FavoriteStore(preferences).all()
            val currentId = preferences.getString(KEY_WALLPAPER_CONTENT_ID, null)
            val eligible = items.filter { mode != RotationMode.FAVORITES_ONLY || it.id in favorites }
            val current = eligible.firstOrNull { it.id == currentId }
            if (current != null) return current

            val selected = if (mode == RotationMode.NEVER) {
                items.firstOrNull()
            } else {
                ContentRotationManager(preferences).next(items, mode, favorites, currentId)
            }
            if (selected != null) {
                preferences.edit().putString(KEY_WALLPAPER_CONTENT_ID, selected.id).apply()
            } else {
                preferences.edit().remove(KEY_WALLPAPER_CONTENT_ID).apply()
            }

            RotationSchedule.nextAt(System.currentTimeMillis(), mode)?.let { deadline ->
                preferences.edit().putLong(KEY_NEXT_ROTATION_AT, deadline).apply()
            }
            return selected
        }

        private fun makeLayout(text: String, paint: TextPaint, width: Int, alignment: Layout.Alignment): StaticLayout =
            StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
                .setAlignment(alignment)
                .setTextDirection(android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR)
                .setIncludePad(true)
                .build()

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
            if (preferences.getBoolean("light_effect", true)) {
                canvas.drawPath(path, ridgePaint)
            }
        }
    }
}
