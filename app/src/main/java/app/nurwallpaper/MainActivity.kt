package app.nurwallpaper

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private val backgroundColorValue = Color.rgb(7, 12, 29)
    private val surface = Color.rgb(17, 26, 48)
    private val accent = Color.rgb(74, 222, 224)
    private lateinit var root: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun showHome() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(28), dp(22), dp(24))
            setBackgroundColor(backgroundColorValue)
        }

        val scroll = ScrollView(this).apply {
            isFillViewport = true
            addView(root)
        }

        root.addView(label("NUR", 13f, accent, true))
        root.addView(label("NurWallpaper", 30f, Color.WHITE, true), margin(top = 5))
        root.addView(label("Erinnerungen, die dich durch den Tag begleiten.", 15f, 0xFFB8C4D9.toInt()), margin(top = 8, bottom = 22))

        root.addView(sectionTitle("DEINE BIBLIOTHEK"))
        root.addView(categoryButton("Verse", "Koranverse mit Quelle und Übersetzung") { showCategory("Verse") }, margin(top = 12))
        root.addView(categoryButton("Hadithe", "Überlieferungen aus Bukhārī und Muslim") { showCategory("Hadithe") }, margin(top = 10))
        root.addView(categoryButton("Duʿāʾ & Dhikr", "Bittgebete und Gedenken") { showCategory("Duʿāʾ & Dhikr") }, margin(top = 10))
        root.addView(categoryButton("Esmaül Hüsna", "Die 99 schönen Namen Allahs") { showCategory("Esmaül Hüsna") }, margin(top = 10))

        root.addView(sectionTitle("WALLPAPER"), margin(top = 26, bottom = 10))
        root.addView(label("Wähle NurWallpaper als Hintergrund für deinen Start- oder Sperrbildschirm.", 14f, 0xFFB8C4D9.toInt()))
        root.addView(actionButton("Live-Wallpaper auswählen") { openWallpaperPicker() }, margin(top = 12))
        root.addView(label("Offline · Ohne Konto · Ohne Werbung", 12f, 0xFF8492AA.toInt()), margin(top = 20))
        setContentView(scroll)
    }

    private fun showCategory(name: String) {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(28), dp(22), dp(24))
            setBackgroundColor(background)
        }
        val scroll = ScrollView(this).apply { isFillViewport = true; addView(root) }
        root.addView(actionButton("← Zurück") { showHome() }, margin(bottom = 28))
        root.addView(label("BIBLIOTHEK", 13f, accent, true))
        root.addView(label(name, 28f, Color.WHITE, true), margin(top = 6, bottom = 18))
        root.addView(label(
            "Dieser Bereich wird gerade mit geprüften Inhalten aufgebaut. Es werden nur Einträge angezeigt, deren Wortlaut, Fundstelle, Übersetzung und Nutzungsrechte ausreichend geprüft sind.",
            16f, 0xFFD1DAE8.toInt()
        ), margin(top = 4))
        root.addView(label(
            "Noch keine Inhalte zur Veröffentlichung freigegeben.",
            14f, accent, true
        ), margin(top = 22))
        setContentView(scroll)
    }

    private fun categoryButton(title: String, subtitle: String, onClick: () -> Unit): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(16), dp(18), dp(16))
            background = rounded(surface, 16)
            isClickable = true
            isFocusable = true
            setOnClickListener { onClick() }
        }
        box.addView(label(title, 19f, Color.WHITE, true))
        box.addView(label(subtitle, 13f, 0xFF9EADC4.toInt()), margin(top = 5))
        return box
    }

    private fun sectionTitle(text: String) = label(text, 12f, accent, true)

    private fun actionButton(text: String, onClick: () -> Unit): Button =
        Button(this).apply {
            this.text = text
            isAllCaps = false
            textSize = 15f
            setTextColor(backgroundColorValue)
            setBackgroundTintList(android.content.res.ColorStateList.valueOf(accent))
            setOnClickListener { onClick() }
        }

    private fun label(text: String, size: Float, color: Int, bold: Boolean = false) =
        TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
            if (bold) typeface = Typeface.DEFAULT_BOLD
        }

    private fun rounded(color: Int, radius: Int) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radius).toFloat()
        setStroke(dp(1), 0xFF263653.toInt())
    }

    private fun margin(top: Int = 0, bottom: Int = 0): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(-1, -2).apply {
            topMargin = dp(top)
            bottomMargin = dp(bottom)
        }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun openWallpaperPicker() {
        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
            putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this@MainActivity, NurWallpaperService::class.java)
            )
        }
        startActivity(intent)
    }
}
