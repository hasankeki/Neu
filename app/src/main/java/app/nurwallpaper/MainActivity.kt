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
        root.addView(actionButton("Gebetszeiten") { startActivity(Intent(this, PrayerTimesActivity::class.java)) }, margin(top = 10))
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
                val translationLanguage = translationLanguageFor(
                    language,
                    prefs.getString("translation_language", "de") ?: "de"
                )
                val translation = item.textFor(translationLanguage)
                if (translation != null) {
                    root.addView(label(translation, 16f, 0xFFF0E7D6.toInt()), margin(top = 10))
                }
                root.addView(label("${item.source.collection} · ${item.source.locator}", 12f, 0xFFE2B453.toInt()), margin(top = 8))
                if (category == ContentCategory.VERSE && Regex("^[1-9][0-9]{0,2}:[1-9][0-9]{0,2}$").matches(item.source.locator.trim())) {
                    root.addView(actionButton("▶ Aya anhören") {
                        startActivity(Intent(this, AudioPlayerActivity::class.java).putExtra(AudioPlayerActivity.EXTRA_VERSE_KEY, item.source.locator.trim()))
                    }, margin(top = 8))
                }
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
        root.addView(label("Übersetzung", 13f, accent, true), margin(top = 22))
        settingRow("Übersetzungssprache bei arabischer Anzeige", translationLanguageLabel(), "Deutsch", "Türkçe", "English") { value ->
            val code = when (value) { "Türkçe" -> "tr"; "English" -> "en"; else -> "de" }
            prefs.edit().putString("translation_language", code).apply()
            showSettings()
        }
        root.addView(label("Der arabische Originaltext bleibt immer sichtbar. Die Übersetzung wird unabhängig davon ausgewählt.", 12f, 0xFFBBA77E.toInt()), margin(top = 8))
        root.addView(label("Rotation", 13f, accent, true), margin(top = 22))
        settingRow("Wie oft soll sich der Inhalt ändern?", rotationLabel(), "Nie", "Selten", "Oft", "Nur Favoriten") { value ->
            prefs.edit().putString("rotation", value).apply()
            showSettings()
        }
        root.addView(label("Oft: alle 30 Minuten · Selten: alle 3 Stunden · Nur Favoriten: alle 30 Minuten, ausschließlich aus deinen Favoriten. Der Wechsel erfolgt nach Zeitintervall, nicht bei jedem Entsperren.", 12f, 0xFFBBA77E.toInt()), margin(top = 8))
        root.addView(label("Gebetszeiten", 13f, accent, true), margin(top = 22))
        settingRow("Berechnungsmethode", prayerMethodLabel(), "Diyanet", "Muslim World League", "ISNA", "Ägypten", "Karachi") { value ->
            val method = when (value) {
                "Muslim World League" -> 3
                "ISNA" -> 2
                "Ägypten" -> 5
                "Karachi" -> 1
                else -> 13
            }
            prefs.edit().putInt("prayer_method", method).apply()
            showSettings()
        }
        settingRow("Asr-Berechnung", asrSchoolLabel(), "Hanafi", "Shafi") { value ->
            prefs.edit().putInt("prayer_school", if (value == "Hanafi") 1 else 0).apply()
            showSettings()
        }
        root.addView(label("Audio", 13f, accent, true), margin(top = 22))
        val audioApiUrl = prefs.getString("audio_api_base_url", "").orEmpty()
        root.addView(categoryButton("Audio-Server verbinden", if (audioApiUrl.isBlank()) "Noch nicht eingerichtet" else audioApiUrl) {
            editAudioApiUrl()
        }, margin(top = 10))
        root.addView(label("Die Wiedergabe startet nur nach deinem Klick und spielt ausschließlich die ausgewählte Aya online ab.", 12f, 0xFFBBA77E.toInt()), margin(top = 8))
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

    private fun editAudioApiUrl() {
        val field = android.widget.EditText(this).apply {
            hint = "https://dein-audio-server.example"
            setSingleLine(true)
            setText(prefs.getString("audio_api_base_url", ""))
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_URI
        }
        AlertDialog.Builder(this)
            .setTitle("Audio-Backend-Adresse")
            .setMessage("Hier kommt später die HTTPS-Adresse des bereitgestellten NurWallpaper-Audio-Servers hinein. Keine Quran-Foundation-Zugangsdaten hier eintragen.")
            .setView(field)
            .setPositiveButton("Speichern") { _, _ ->
                val value = field.text.toString().trim().trimEnd('/')
                if (value.isBlank() || value.startsWith("https://")) {
                    prefs.edit().putString("audio_api_base_url", value).apply()
                    showSettings()
                } else {
                    AlertDialog.Builder(this)
                        .setMessage("Bitte eine sichere HTTPS-Adresse eingeben.")
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }

    private fun showSources() {
        val scroll = page()
        root.addView(actionButton("← Zurück zu Einstellungen") { showSettings() }, margin(bottom = 24))
        root.addView(label("Quellen und Lizenzen", 26f, Color.WHITE, true))
        root.addView(label("Korantext: Tanzil Quran Text (Uthmani, Version 1.1). Copyright (C) 2007–2021 Tanzil Project. Lizenz: Creative Commons Attribution 3.0 (CC BY 3.0). Der arabische Text wird wortgetreu übernommen und nicht verändert. Quelle und Updates: tanzil.net", 14f, 0xFFF0E7D6.toInt()), margin(top = 18))
        root.addView(label("Hadith-Primärsammlungen: ausschließlich Ṣaḥīḥ al-Bukhārī und Ṣaḥīḥ Muslim. Die drei derzeit freigegebenen mehrsprachigen Übersetzungen stammen von HadeethEnc.com / Rowad Translation Center, veröffentlicht durch das Islamic Propagation Office in Rabwah. Sprachversionen: Deutsch v1.58.0 · Türkisch v1.71.0 · Englisch v1.25.0. Die Texte werden unverändert und mit Quellen-/Versionsangabe geführt.", 14f, 0xFFF0E7D6.toInt()), margin(top = 14))
        root.addView(label("Sunnah.com dient bei den noch nicht freigegebenen Einträgen ausschließlich zur Fundstellen- und Wortlautkontrolle; dortige moderne Übersetzungen werden nicht ohne eigene Lizenz übernommen. Quran-Übersetzungen benötigen je Sprache eine dokumentierte Lizenz, Versionsangabe und Attribution.", 13f, 0xFFBBA77E.toInt()), margin(top = 14))
        root.addView(label("Ein Inhalt wird erst veröffentlicht, wenn Originalwortlaut, genaue Fundstelle, Übersetzung, Nutzungsrechte und fachliche Prüfung dokumentiert sind.", 14f, accent), margin(top = 16))
        setContentView(scroll)
    }

    private fun languageLabel() = when (prefs.getString("language", "de")) {
        "tr" -> "Türkçe"; "en" -> "English"; "ar" -> "العربية"; else -> "Deutsch"
    }

    private fun translationLanguageLabel() = when (prefs.getString("translation_language", "de")) {
        "tr" -> "Türkçe"; "en" -> "English"; else -> "Deutsch"
    }

    private fun rotationLabel() = prefs.getString("rotation", "Nie") ?: "Nie"
    
    private fun prayerMethodLabel() = when (prefs.getInt("prayer_method", 13)) {
        3 -> "Muslim World League"; 2 -> "ISNA"; 5 -> "Ägypten"; 1 -> "Karachi"; else -> "Diyanet"
    }

    private fun asrSchoolLabel() = if (prefs.getInt("prayer_school", 1) == 0) "Shafi" else "Hanafi"

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
