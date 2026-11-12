package app.nurwallpaper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.service.wallpaper.WallpaperService
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.content.SharedPreferences
import android.view.MotionEvent
import android.view.SurfaceHolder
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

private const val KEY_WALLPAPER_CONTENT_ID = "wallpaper_content_id"
private const val KEY_NEXT_ROTATION_AT = "wallpaper_next_rotation_at"

private object GoldenPalette {
    val black = Color.rgb(5, 5, 7)
    val deepBlack = Color.rgb(10, 8, 7)
    val gold = Color.rgb(226, 180, 83)
    val brightGold = Color.rgb(255, 218, 135)
    val softGold = Color.rgb(177, 127, 45)
    val ivory = Color.rgb(248, 239, 218)
    val mutedGold = Color.rgb(205, 177, 125)
}

class NurWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine = NurEngine()

    private inner class NurEngine : Engine() {
        private val preferences by lazy {
            getSharedPreferences("nurwallpaper_settings", MODE_PRIVATE)
        }
        private val rotationHandler = Handler(Looper.getMainLooper())
        private val visualHandler = Handler(Looper.getMainLooper())
        private val animationStartedAt = SystemClock.uptimeMillis()
        private var touchStartX = 0f
        private var touchStartY = 0f
        private var touchTracking = false
        private val visualTick = object : Runnable {
            override fun run() {
                if (isVisible && preferences.getBoolean("light_effect", true)) {
                    drawFrame()
                    visualHandler.postDelayed(this, 50L)
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
            if (key == "language" || key == "light_effect" || key == "rotation" || key == "favorite_content_ids") {
                drawFrame()
                scheduleVisualAnimation()
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            preferences.registerOnSharedPreferenceChangeListener(settingsListener)
            // Live wallpapers do not receive home-screen gestures unless explicitly enabled.
            setTouchEventsEnabled(true)
        }

        override fun onDestroy() {
            rotationHandler.removeCallbacks(rotationTick)
            visualHandler.removeCallbacks(visualTick)
            preferences.unregisterOnSharedPreferenceChangeListener(settingsListener)
            super.onDestroy()
        }

        private val contentRepository by lazy {
            runCatching { ContentLibraryLoader.load(this@NurWallpaperService) }
                .getOrElse { ContentRepository() }
        }
        private val contentPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = GoldenPalette.brightGold
            textSize = 38f
            textAlign = Paint.Align.CENTER
            setShadowLayer(12f, 0f, 2f, Color.BLACK)
        }
        private val translationPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = GoldenPalette.ivory
            textSize = 24f
            textAlign = Paint.Align.CENTER
            setShadowLayer(8f, 0f, 1f, Color.BLACK)
        }
        private val sourcePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = GoldenPalette.mutedGold
            textSize = 17f
            textAlign = Paint.Align.CENTER
            setShadowLayer(4f, 0f, 1f, Color.BLACK)
        }
        private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val ornamentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.4f
            color = 0x55DDAF55
        }
        private val sweepPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3.2f
            strokeCap = Paint.Cap.ROUND
            setShadowLayer(18f, 0f, 0f, GoldenPalette.gold)
        }
        private val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val panelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xAAE2B453.toInt()
            strokeWidth = 1.3f
        }
        private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = GoldenPalette.brightGold
            textAlign = Paint.Align.CENTER
            textSize = 54f
            setShadowLayer(16f, 0f, 2f, GoldenPalette.softGold)
        }
        private val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = GoldenPalette.ivory
            textAlign = Paint.Align.CENTER
            textSize = 25f
            setShadowLayer(7f, 0f, 1f, Color.BLACK)
        }

        override fun onTouchEvent(event: MotionEvent) {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    touchStartX = event.x
                    touchStartY = event.y
                    touchTracking = true
                }
                MotionEvent.ACTION_UP -> {
                    if (touchTracking) {
                        val deltaX = event.x - touchStartX
                        val deltaY = event.y - touchStartY
                        val threshold = 64f * resources.displayMetrics.density
                        // Swipe right selects a random new item; swipe left goes back in history.
                        if (abs(deltaX) >= threshold && abs(deltaX) > abs(deltaY) * 1.25f) {
                            if (deltaX > 0f) showRandomNextContent() else showPreviousContent()
                        }
                    }
                    touchTracking = false
                }
                MotionEvent.ACTION_CANCEL -> touchTracking = false
            }
            super.onTouchEvent(event)
        }

        private fun eligibleSwipeContent(): List<ContentItem> {
            val approved = contentRepository.approved()
            val mode = RotationMode.fromSetting(preferences.getString("rotation", "Nie"))
            val favorites = FavoriteStore(preferences).all()
            return if (mode == RotationMode.FAVORITES_ONLY) {
                approved.filter { it.id in favorites }
            } else {
                approved
            }
        }

        private fun readSwipeHistory(): MutableList<String> {
            val stored = preferences.getString("swipe_content_history", "").orEmpty()
            return stored.split("|").filter { it.isNotBlank() }.toMutableList()
        }

        private fun saveSwipeHistory(history: List<String>, index: Int) {
            preferences.edit()
                .putString("swipe_content_history", history.joinToString("|"))
                .putInt("swipe_content_history_index", index)
                .apply()
        }

        /** Swipe right: pick a random eligible item, without following library order. */
        private fun showRandomNextContent() {
            val pool = eligibleSwipeContent()
            if (pool.isEmpty()) return

            val currentId = preferences.getString(KEY_WALLPAPER_CONTENT_ID, null)
            val history = readSwipeHistory()
            var index = preferences.getInt("swipe_content_history_index", -1).coerceIn(-1, history.lastIndex)

            // If the user went back, moving right replays the already-seen next item.
            if (index >= 0 && index < history.lastIndex) {
                val nextId = history[index + 1]
                val next = pool.firstOrNull { it.id == nextId }
                if (next != null) {
                    saveSwipeHistory(history, index + 1)
                    preferences.edit().putString(KEY_WALLPAPER_CONTENT_ID, next.id).apply()
                    drawFrame()
                    return
                }
            }

            val candidates = pool.filter { it.id != currentId }
            if (candidates.isEmpty()) return
            val selected = candidates.random()
            val validHistory = history.take(index + 1).filter { id -> pool.any { it.id == id } }.toMutableList()
            if (validHistory.isEmpty() && currentId != null && pool.any { it.id == currentId }) {
                validHistory.add(currentId)
            }
            if (validHistory.lastOrNull() != currentId && currentId != null && pool.any { it.id == currentId }) {
                validHistory.add(currentId)
            }
            validHistory.add(selected.id)
            saveSwipeHistory(validHistory, validHistory.lastIndex)
            preferences.edit().putString(KEY_WALLPAPER_CONTENT_ID, selected.id).apply()
            drawFrame()
        }

        /** Swipe left: return to the previously displayed eligible item. */
        private fun showPreviousContent() {
            val pool = eligibleSwipeContent()
            if (pool.isEmpty()) return

            val history = readSwipeHistory()
            var index = preferences.getInt("swipe_content_history_index", -1).coerceIn(-1, history.lastIndex)
            if (index < 0 || history.isEmpty()) {
                val currentId = preferences.getString(KEY_WALLPAPER_CONTENT_ID, null)
                if (currentId != null && pool.any { it.id == currentId }) {
                    saveSwipeHistory(listOf(currentId), 0)
                }
                return
            }

            var previousIndex = index - 1
            while (previousIndex >= 0 && pool.none { it.id == history[previousIndex] }) {
                previousIndex--
            }
            if (previousIndex < 0) return

            val previous = pool.firstOrNull { it.id == history[previousIndex] } ?: return
            saveSwipeHistory(history, previousIndex)
            preferences.edit().putString(KEY_WALLPAPER_CONTENT_ID, previous.id).apply()
            drawFrame()
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
                scheduleVisualAnimation()
            } else {
                rotationHandler.removeCallbacks(rotationTick)
                visualHandler.removeCallbacks(visualTick)
            }
        }

        private fun scheduleVisualAnimation() {
            visualHandler.removeCallbacks(visualTick)
            if (isVisible && preferences.getBoolean("light_effect", true)) {
                visualHandler.postDelayed(visualTick, 50L)
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
            if (resetDeadline || shouldRotate || nextAt <= 0L) {
                preferences.edit().putLong(KEY_NEXT_ROTATION_AT, now + interval).apply()
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
            val centerX = w / 2f
            val centerY = h * 0.48f
            val animated = preferences.getBoolean("light_effect", true)
            val elapsed = (SystemClock.uptimeMillis() - animationStartedAt) / 1000f
            val phase = if (animated) elapsed else 0f

            // Deep black foundation with a warm, subtle golden atmosphere.
            canvas.drawColor(GoldenPalette.black)
            backgroundPaint.shader = RadialGradient(
                centerX, centerY, w * 0.82f,
                intArrayOf(0x302B1B0B, 0x181A1209, 0x00000000),
                floatArrayOf(0f, 0.62f, 1f),
                Shader.TileMode.CLAMP
            )
            canvas.drawRect(0f, 0f, w, h, backgroundPaint)
            backgroundPaint.shader = null

            // Fine ornamental geometry frames the content without competing with it.
            val ornament = RectF(w * 0.075f, h * 0.19f, w * 0.925f, h * 0.78f)
            canvas.drawRoundRect(ornament, 42f, 42f, ornamentPaint)
            val inner = RectF(w * 0.095f, h * 0.205f, w * 0.905f, h * 0.765f)
            ornamentPaint.alpha = 95
            canvas.drawRoundRect(inner, 36f, 36f, ornamentPaint)
            ornamentPaint.alpha = 255

            // A soft halo adds warmth behind the verse.
            glowPaint.shader = RadialGradient(
                centerX, centerY, w * 0.52f,
                intArrayOf(0x35E8B85A, 0x16B77B32, 0x00000000),
                floatArrayOf(0f, 0.48f, 1f),
                Shader.TileMode.CLAMP
            )
            canvas.drawCircle(centerX, centerY, w * 0.52f, glowPaint)
            glowPaint.shader = null

            if (animated) {
                drawGoldenFlow(canvas, w, h, phase)
            } else {
                drawGoldDust(canvas, w, h, 0f, false)
            }

            val panel = RectF(w * 0.055f, centerY - h * 0.18f, w * 0.945f, centerY + h * 0.19f)
            panelPaint.shader = LinearGradient(
                panel.left, panel.top, panel.right, panel.bottom,
                intArrayOf(0x00130E08, 0x7D0B0907, 0x00130E08),
                null, Shader.TileMode.CLAMP
            )
            canvas.drawRoundRect(panel, 28f, 28f, panelPaint)
            panelPaint.shader = null

            val firstApproved = selectedWallpaperContent()
            if (firstApproved == null) {
                canvas.drawText("NurWallpaper", centerX, centerY - 5f, titlePaint)
                val language = preferences.getString("language", "de") ?: "de"
                val tagline = when (language) {
                    "tr" -> "Önce mesaj, sonra ışık."
                    "en" -> "The message comes before the light."
                    "ar" -> "الرسالة أولاً، ثم النور."
                    else -> "Erst die Botschaft. Dann das Licht."
                }
                canvas.drawText(tagline, centerX, centerY + 43f, subtitlePaint)
            } else {
                val width = (w * 0.82f).toInt().coerceAtLeast(1)
                val language = preferences.getString("language", "de") ?: "de"
                val arabic = makeLayout(firstApproved.originalText, contentPaint, width, Layout.Alignment.ALIGN_CENTER)
                val translatedText = if (language == "ar") null else firstApproved.textFor(language)
                val translation = translatedText?.let {
                    makeLayout(it, translationPaint, width, Layout.Alignment.ALIGN_CENTER)
                }
                val blockHeight = arabic.height + (translation?.height ?: 0) + 46f
                val top = (centerY - blockHeight / 2f).coerceAtLeast(h * 0.23f)
                canvas.save()
                canvas.translate((w - width) / 2f, top)
                arabic.draw(canvas)
                canvas.restore()
                var nextY = top + arabic.height + 20f
                if (translation != null) {
                    canvas.save()
                    canvas.translate((w - width) / 2f, nextY)
                    translation.draw(canvas)
                    canvas.restore()
                    nextY += translation.height + 14f
                }
                canvas.drawLine(w * 0.34f, nextY + 4f, w * 0.66f, nextY + 4f, dividerPaint)
                canvas.drawText(
                    "${firstApproved.source.collection} · ${firstApproved.source.locator}",
                    centerX, nextY + 30f, sourcePaint
                )
            }
        }

        private fun drawGoldenFlow(canvas: Canvas, width: Float, height: Float, phase: Float) {
            val sweep = ((phase * 0.11f) % 1.35f) - 0.18f
            val path = Path().apply {
                moveTo(width * (sweep - 0.12f), height * 0.31f)
                cubicTo(
                    width * (sweep + 0.18f), height * 0.19f,
                    width * (sweep + 0.34f), height * 0.55f,
                    width * (sweep + 0.66f), height * 0.43f
                )
            }
            sweepPaint.shader = LinearGradient(
                width * sweep, 0f, width * (sweep + 0.7f), height * 0.5f,
                intArrayOf(0x00E2B453, 0xFFE8BD69.toInt(), 0x00E2B453),
                null, Shader.TileMode.CLAMP
            )
            canvas.drawPath(path, sweepPaint)
            sweepPaint.strokeWidth = 1.2f
            canvas.drawPath(path, sweepPaint)
            sweepPaint.strokeWidth = 3.2f
            sweepPaint.shader = null

            val second = Path().apply {
                moveTo(width * 0.08f, height * 0.68f)
                cubicTo(
                    width * (0.28f + 0.035f * sin(phase * 0.45f)), height * 0.56f,
                    width * (0.65f - 0.035f * sin(phase * 0.35f)), height * 0.79f,
                    width * 0.94f, height * 0.64f
                )
            }
            sweepPaint.alpha = 105
            canvas.drawPath(second, sweepPaint)
            sweepPaint.alpha = 255
            drawGoldDust(canvas, width, height, phase, true)
        }

        private fun drawGoldDust(canvas: Canvas, width: Float, height: Float, phase: Float, moving: Boolean) {
            val random = Random(2409)
            repeat(52) { index ->
                val baseX = random.nextFloat() * width
                val baseY = random.nextFloat() * height
                val drift = if (moving) sin(phase * 0.55f + index * 0.73f) * 10f else 0f
                val twinkle = if (moving) (0.55f + 0.45f * sin(phase * 1.2f + index)).coerceIn(0.12f, 1f) else 0.62f
                particlePaint.color = GoldenPalette.brightGold
                particlePaint.alpha = (twinkle * if (index % 5 == 0) 190 else 105).toInt()
                val radius = if (index % 7 == 0) 2.2f else 1.0f + random.nextFloat() * 0.8f
                canvas.drawCircle(baseX + drift, baseY, radius, particlePaint)
            }
            particlePaint.alpha = 255
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
            if (selected != null) preferences.edit().putString(KEY_WALLPAPER_CONTENT_ID, selected.id).apply()
            else preferences.edit().remove(KEY_WALLPAPER_CONTENT_ID).apply()

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
    }
}
