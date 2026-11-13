package app.nurwallpaper

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import android.text.TextPaint
import android.text.Layout
import android.text.StaticLayout
import kotlin.math.sin

/** Clean, edge-to-edge preview. Tap once to reveal controls; tap the wallpaper to hide them. */
class WallpaperPreviewActivity : Activity() {
    private lateinit var controls: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.BLACK
        hideSystemBars()

        val frame = FrameLayout(this)
        val wallpaper = PreviewWallpaperView()
        frame.addView(wallpaper, FrameLayout.LayoutParams(-1, -1))

        controls = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(18, 14, 18, 14)
            setBackgroundColor(0xCC080706.toInt())
            visibility = View.GONE
            addView(Button(this@WallpaperPreviewActivity).apply {
                text = "Zurück"
                isAllCaps = false
                setOnClickListener { finish() }
            })
            addView(Button(this@WallpaperPreviewActivity).apply {
                text = "Als Hintergrund festlegen"
                isAllCaps = false
                setOnClickListener {
                    startActivity(Intent(android.app.WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                        putExtra(
                            android.app.WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            android.content.ComponentName(this@WallpaperPreviewActivity, NurWallpaperService::class.java)
                        )
                    })
                }
            })
        }
        val controlsParams = FrameLayout.LayoutParams(-1, -2, Gravity.BOTTOM)
        frame.addView(controls, controlsParams)
        wallpaper.onTap = { controls.visibility = if (controls.visibility == View.VISIBLE) View.GONE else View.VISIBLE }
        setContentView(frame)
    }

    @Suppress("DEPRECATION")
    private fun hideSystemBars() {
        if (android.os.Build.VERSION.SDK_INT >= 30) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
        }
    }

    private inner class PreviewWallpaperView : View(this@WallpaperPreviewActivity) {
        var onTap: (() -> Unit)? = null
        private val prefs = getSharedPreferences("nurwallpaper_settings", MODE_PRIVATE)
        private val item = runCatching {
            val repository = ContentLibraryLoader.load(this@WallpaperPreviewActivity)
            val selectedId = prefs.getString("wallpaper_content_id", null)
            repository.findApproved(selectedId ?: "") ?: repository.approved().firstOrNull()
        }.getOrNull()
        private val arabicPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFD98A.toInt()
            textSize = 38f
            textAlign = Paint.Align.CENTER
            setShadowLayer(12f, 0f, 2f, Color.BLACK)
        }
        private val translationPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFF8EFDA.toInt()
            textSize = 23f
            textAlign = Paint.Align.CENTER
            setShadowLayer(8f, 0f, 1f, Color.BLACK)
        }
        private val sourcePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFCDB17D.toInt()
            textSize = 16f
            textAlign = Paint.Align.CENTER
        }
        private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFD98A.toInt()
            textSize = 48f
            textAlign = Paint.Align.CENTER
        }
        private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val w = width.toFloat()
            val h = height.toFloat()
            val cx = w / 2f
            val cy = h * 0.48f
            val design = prefs.getString("wallpaper_design", "dreamscape") ?: "dreamscape"
            val animated = prefs.getBoolean("light_effect", true)
            val phase = android.os.SystemClock.uptimeMillis() / 1000f
            WallpaperDesignRenderer.draw(canvas, design, phase, animated)
            if (animated) postInvalidateDelayed(50L)

            val content = item
            if (content == null) {
                canvas.drawText("NurWallpaper", cx, cy, titlePaint)
                val hint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = 0xFFE8DDC7.toInt()
                    textSize = 19f
                    textAlign = Paint.Align.CENTER
                }
                canvas.drawText("Erst die Botschaft. Dann das Licht.", cx, cy + 42f, hint)
                return
            }
            val language = prefs.getString("language", "de") ?: "de"
            val widthPx = (w * .82f).toInt().coerceAtLeast(1)
            val arabic = makeLayout(content.originalText, arabicPaint, widthPx)
            val translationText = if (language == "ar") null else content.textFor(language)
            val translation = translationText?.let { makeLayout(it, translationPaint, widthPx) }
            val total = arabic.height + (translation?.height ?: 0) + 55f
            var y = (cy - total / 2f).coerceAtLeast(h * .22f)
            canvas.save()
            canvas.translate((w - widthPx) / 2f, y)
            arabic.draw(canvas)
            canvas.restore()
            y += arabic.height + 18f
            if (translation != null) {
                canvas.save()
                canvas.translate((w - widthPx) / 2f, y)
                translation.draw(canvas)
                canvas.restore()
                y += translation.height + 12f
            }
            val line = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xAAE2B453.toInt(); strokeWidth = 1.2f }
            canvas.drawLine(w * .34f, y + 2f, w * .66f, y + 2f, line)
            canvas.drawText("${content.source.collection} · ${content.source.locator}", cx, y + 28f, sourcePaint)
        }

        private fun makeLayout(text: String, paint: TextPaint, width: Int): StaticLayout =
            StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setTextDirection(android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR)
                .setIncludePad(true)
                .build()

        override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                onTap?.invoke()
                return true
            }
            return true
        }
    }
}
