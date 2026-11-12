package app.nurwallpaper

import android.app.Activity
import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private val backgroundColorValue = Color.rgb(7, 6, 5)
    private val surface = Color.rgb(24, 19, 12)
    private val accent = Color.rgb(226, 180, 83)
    private val prefs by lazy { getSharedPreferences("nurwallpaper_settings", MODE_PRIVATE) }
    private lateinit var root: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(7, 6, 5)
        window.navigationBarColor = Color.rgb(7, 6, 5)
        showHome()
    }

    private fun page(): ScrollView {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(28), dp(22), dp(24))
            setBackgroundColor(backgroundColorValue)
        }
        return ScrollView(this).apply { isFillViewport = true; addView(root) }
    }

    private fun showHome() {
        val scroll = page()
        root.addView(label("NUR", 13f, accent, true))
        root.addView(label("NurWallpaper", 30f, Color.WHITE, true), margin(top = 5))
        root.addView(label("Erinnerungen, die dich durch den Tag begleiten.", 15f, 0xFFE8DDC7.toInt()), margin(top = 8, bottom = 22))
        root.addView(sectionTitle("DEINE BIBLIOTHEK"))
        root.addView(categoryButton("Verse", "Koranverse mit Quelle und Übersetzung") { showCategory("Verse") }, margin(top = 12))
        root.addView(categoryButton("Hadithe", "Ausschließlich Bukhārī und Muslim") { showCategory("Hadithe") }, margin(top = 10))
        root.addView(categoryButton("Duʿāʾ & Dhikr", "Bittgebete und Gedenken") { showCategory("Duʿāʾ & Dhikr") }, margin(top = 10))
        root.addView(categoryButton("Esmaül Hüsna", "Die 99 schönen Namen Allahs") { showCategory("Esmaül Hüsna") }, margin(top = 10))
        root.addView(sectionTitle("WALLPAPER"), margin(top = 26, bottom = 10))
        root.addView(label("Wähle NurWallpaper als Hintergrund für deinen Start- oder Sperrbildschirm.", 14f, 0xFFE8DDC7.toInt()))
        root.addView(actionButton("Vollbild-Vorschau") { startActivity(Intent(this, WallpaperPreviewActivity::class.java)) }, margin(top = 12))
        root.addView(actionButton("Live-Wallpaper auswählen") { openWallpaperPicker() }, margin(top = 10))
        root.addView(actionButton("Qibla finden") { startActivity(Intent(this, QiblaActivity::class.java)) }, margin(top = 10))
        root.addView(actionButton("Einstellungen") { showSettings() }, margin(top = 10))
        root.addView(label("Offline · Ohne Konto · Ohne Werbung", 12f, 0xFF9D855D.toInt()), margin(top = 20))
        setContentView(scroll)
    }

    private fun showCategory(name: String) {
        val scroll = page()
        root.addView(actionButton("← Zurück") { showHome() }, margin(bottom = 28))
        root.addView(label("BIBLIOTHEK", 13f, accent, true))
        root.addView(label(name, 28f, Color.WHITE, true), margin(top = 6, bottom = 18))
        val category = when (name) {
            "Verse" -> ContentCategory.VERSE
            "Hadithe" -> ContentCategory.HADITH
            "Duʿāʾ & Dhikr" -> ContentCategory.DUA_DHIKR
            "Esmaül Hüsna" -> ContentCategory.ASMA_UL_HUSNA
            else -> null
        }
        val language = prefs.getString("language", "de") ?: "de"
        val repository = runCatching { ContentLibraryLoader.load(this) }.getOrElse {
            root.addView(label("Die lokale Inhaltsdatei konnte nicht gelesen werden.", 14f, 0xFFFFB4AB.toInt()))
            setContentView(scroll)
            return
        }
        val items = repository.approved(category)
        if (items.isEmpty()) {
            root.addView(label("Noch keine Inhalte zur Veröffentlichung freigegeben.", 15f, 0xFFF0E7D6.toInt()), margin(top = 4))
            root.addView(label("Ungeprüfte oder unvollständige Einträge bleiben bewusst unsichtbar. Sobald geprüfte Inhalte samt Übersetzungen und Lizenzangaben freigegeben sind, erscheinen sie hier automatisch.", 13f, 0xFFBBA77E.toInt()), margin(top = 12))
        } else {
            val favorites = FavoriteStore(prefs)
            items.forEach { item ->
                root.addView(label(item.originalText, 21f, 0xFFFFD98A.toInt(), true), margin(top = 16))
                val translation = item.textFor(language)
                if (translation != null) {
                    root.addView(label(translation, 16f, 0xFFF0E7D6.toInt()), margin(top = 10))
                }
                root.addView(label("${item.source.collection} · ${item.source.locator}", 12f, 0xFFE2B453.toInt()), margin(top = 8))
                root.addView(actionButton(
                    if (favorites.contains(item.id)) "★ Favorit entfernen" else "☆ Als Favorit speichern"
                ) {
                    favorites.toggle(item.id)
                    showCategory(name)
                }, margin(top = 6))
            }
        }
        setContentView(scroll)
    }

    private fun showSettings() {
        val scroll = page()
        root.addView(actionButton("← Zurück") { showHome() }, margin(bottom = 24))
        root.addView(label("Einstellungen", 28f, Color.WHITE, true))
        root.addView(label("Sprache", 13f, accent, true), margin(top = 24))
        settingRow("Anzeigesprache", languageLabel(), "Deutsch", "Türkçe", "English", "العربية") { value ->
            val code = when (value) { "Türkçe" -> "tr"; "English" -> "en"; "العربية" -> "ar"; else -> "de" }
            prefs.edit().putString("language", code).apply()
            showSettings()
        }
        root.addView(label("Rotation", 13f, accent, true), margin(top = 22))
        settingRow("Wie oft soll sich der Inhalt ändern?", rotationLabel(), "Nie", "Selten", "Oft", "Nur Favoriten") { value ->
            prefs.edit().putString("rotation", value).apply()
            showSettings()
        }
        root.addView(label("Oft: alle 30 Minuten · Selten: alle 3 Stunden · Nur Favoriten: alle 30 Minuten, ausschließlich aus deinen Favoriten. Der Wechsel erfolgt nach Zeitintervall, nicht bei jedem Entsperren.", 12f, 0xFFBBA77E.toInt()), margin(top = 8))
        root.addView(label("Darstellung", 13f, accent, true), margin(top = 22))
        val animationEnabled = prefs.getBoolean("light_effect", true)
        root.addView(categoryButton("Lichtstreifen", if (animationEnabled) "Aktiviert" else "Deaktiviert") {
            prefs.edit().putBoolean("light_effect", !animationEnabled).apply()
            showSettings()
        }, margin(top = 10))
        root.addView(label("Der dynamische Kontrastschutz bleibt immer aktiv, damit arabischer Text, Übersetzung und Quelle lesbar bleiben.", 13f, 0xFFE8DDC7.toInt()), margin(top = 18))
        root.addView(actionButton("Quellen und Lizenzen") { showSources() }, margin(top = 24))
        setContentView(scroll)
    }

    private fun showSources() {
        val scroll = page()
        root.addView(actionButton("← Zurück zu Einstellungen") { showSettings() }, margin(bottom = 24))
        root.addView(label("Quellen und Lizenzen", 26f, Color.WHITE, true))
        root.addView(label("Korantext: vorgesehene Grundlage ist die Tanzil-Ausgabe. Übersetzungen benötigen jeweils eine eigene Rechteprüfung und Attribution. Hadith-Quellen der offiziellen Bibliothek: Ṣaḥīḥ al-Bukhārī und Ṣaḥīḥ Muslim.", 15f, 0xFFF0E7D6.toInt()), margin(top = 18))
        root.addView(label("Ein Inhalt wird erst veröffentlicht, wenn Originalwortlaut, genaue Fundstelle, Übersetzung, Nutzungsrechte und fachliche Prüfung dokumentiert sind.", 14f, accent), margin(top = 16))
        setContentView(scroll)
    }

    private fun languageLabel() = when (prefs.getString("language", "de")) {
        "tr" -> "Türkçe"; "en" -> "English"; "ar" -> "العربية"; else -> "Deutsch"
    }

    private fun rotationLabel() = prefs.getString("rotation", "Nie") ?: "Nie"

    private fun settingRow(title: String, current: String, vararg options: String, onChoose: (String) -> Unit) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = rounded(surface, 14)
            isClickable = true
            isFocusable = true
            setOnClickListener {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle(title)
                    .setSingleChoiceItems(options, options.indexOf(current)) { dialog, which ->
                        dialog.dismiss()
                        onChoose(options[which])
                    }.setNegativeButton("Abbrechen", null).show()
            }
        }
        row.addView(label(title, 15f, Color.WHITE, true))
        row.addView(label(current + "  ›", 14f, accent), margin(top = 6))
        root.addView(row, margin(top = 9))
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
        box.addView(label(subtitle, 13f, 0xFFBBA77E.toInt()), margin(top = 5))
        return box
    }

    private fun sectionTitle(text: String) = label(text, 12f, accent, true)

    private fun actionButton(text: String, onClick: () -> Unit): Button =
        Button(this).apply {
            this.text = text
            isAllCaps = false
            textSize = 15f
            setTextColor(Color.rgb(20, 15, 8))
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
        setStroke(dp(1), 0xFF6D5125.toInt())
    }

    private fun margin(top: Int = 0, bottom: Int = 0): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(-1, -2).apply {
            topMargin = dp(top)
            bottomMargin = dp(bottom)
        }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun openWallpaperPicker() {
        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
            putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, ComponentName(this@MainActivity, NurWallpaperService::class.java))
        }
        startActivity(intent)
    }
}
