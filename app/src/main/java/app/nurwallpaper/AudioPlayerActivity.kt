package app.nurwallpaper

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.Executors

class AudioPlayerActivity : Activity() {
    companion object {
        const val EXTRA_VERSE_KEY = "verse_key"
    }

    private val prefs by lazy { getSharedPreferences("nurwallpaper_settings", MODE_PRIVATE) }
    private val executor = Executors.newSingleThreadExecutor()
    private var player: MediaPlayer? = null
    private var reciters: List<Reciter> = emptyList()
    private var selectedReciter: Reciter? = null
    private var speed = 1.0f
    private lateinit var status: TextView
    private lateinit var reciterButton: Button
    private lateinit var playButton: Button
    private lateinit var speedLabel: TextView
    private lateinit var speedBar: SeekBar
    private lateinit var verseKey: String

    private data class Reciter(val id: Int, val name: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verseKey = intent.getStringExtra(EXTRA_VERSE_KEY).orEmpty()
        if (!Regex("^[1-9][0-9]{0,2}:[1-9][0-9]{0,2}$").matches(verseKey)) {
            finish()
            return
        }
        buildUi()
        loadReciters()
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(24), dp(30), dp(24), dp(24))
            setBackgroundColor(Color.rgb(7, 6, 5))
        }
        root.addView(TextView(this).apply {
            text = "AYA ANHÖREN"
            textSize = 12f
            setTextColor(Color.rgb(226, 180, 83))
            typeface = Typeface.DEFAULT_BOLD
        })
        root.addView(TextView(this).apply {
            text = "Koranvers $verseKey"
            textSize = 27f
            setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
        }, params(top = 8, bottom = 20))
        status = TextView(this).apply {
            text = "Verbinde mit dem Audio-Dienst …"
            textSize = 14f
            setTextColor(Color.rgb(232, 221, 199))
        }
        root.addView(status, params(bottom = 20))
        reciterButton = Button(this).apply {
            text = "Rezitator auswählen"
            isAllCaps = false
            isEnabled = false
            setOnClickListener { chooseReciter() }
        }
        root.addView(reciterButton, params(bottom = 16))
        val controls = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER_VERTICAL
        }
        playButton = Button(this).apply {
            text = "▶ Abspielen"
            isAllCaps = false
            isEnabled = false
            setOnClickListener { playSelectedVerse() }
        }
        controls.addView(playButton, LinearLayout.LayoutParams(0, -2, 1f))
        speedLabel = TextView(this).apply {
            text = "1,0×"
            textSize = 14f
            setTextColor(Color.rgb(226, 180, 83))
            gravity = android.view.Gravity.CENTER
        }
        controls.addView(speedLabel, LinearLayout.LayoutParams(dp(58), -2))
        root.addView(controls, params(bottom = 4))
        speedBar = SeekBar(this).apply {
            max = 100
            progress = 50
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    speed = 0.5f + progress / 100f
                    speedLabel.text = String.format(java.util.Locale.GERMANY, "%.1f×", speed)
                    if (player?.isPlaying == true && android.os.Build.VERSION.SDK_INT >= 23) {
                        runCatching { player?.setPlaybackParams(PlaybackParams().setSpeed(speed)) }
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            })
        }
        root.addView(speedBar)
        root.addView(TextView(this).apply {
            text = "Die Audiodatei wird nur bei Wiedergabe online geladen. Es gibt keinen automatischen Start und keine Offline-Audiospeicherung."
            textSize = 12f
            setTextColor(Color.rgb(187, 167, 126))
        }, params(top = 18))
        root.addView(Button(this).apply {
            text = "Zurück"
            isAllCaps = false
            setOnClickListener { finish() }
        }, params(top = 24))
        setContentView(root)
    }

    private fun loadReciters() {
        val base = prefs.getString("audio_api_base_url", "").orEmpty().trimEnd('/')
        if (!base.startsWith("https://")) {
            status.text = "Der Audio-Server ist noch nicht verbunden. Richte zuerst die HTTPS-Adresse unter Einstellungen → Audio ein."
            return
        }
        status.text = "Lade verfügbare Rezitatoren …"
        executor.execute {
            try {
                val json = requestJson("$base/v1/reciters")
                val list = json.optJSONArray("reciters") ?: throw IllegalStateException("Keine Rezitatoren erhalten.")
                val parsed = (0 until list.length()).mapNotNull { index ->
                    val item = list.optJSONObject(index) ?: return@mapNotNull null
                    val id = item.optInt("id", -1)
                    val name = item.optString("name", "")
                    if (id > 0 && name.isNotBlank()) Reciter(id, name) else null
                }
                runOnUiThread {
                    reciters = parsed
                    selectedReciter = parsed.firstOrNull()
                    reciterButton.isEnabled = parsed.isNotEmpty()
                    playButton.isEnabled = selectedReciter != null
                    reciterButton.text = selectedReciter?.name ?: "Keine Rezitatoren verfügbar"
                    status.text = if (parsed.isEmpty()) "Der Dienst hat keine Rezitatoren gemeldet." else "Quelle: Quran Foundation · Wähle einen Rezitator und tippe auf Abspielen."
                }
            } catch (error: Exception) {
                runOnUiThread { status.text = "Audio-Dienst nicht erreichbar: ${error.message ?: "Unbekannter Fehler"}" }
            }
        }
    }

    private fun chooseReciter() {
        if (reciters.isEmpty()) return
        AlertDialog.Builder(this)
            .setTitle("Rezitator")
            .setSingleChoiceItems(reciters.map { it.name }.toTypedArray(), reciters.indexOf(selectedReciter)) { dialog, which ->
                selectedReciter = reciters[which]
                reciterButton.text = selectedReciter?.name ?: "Rezitator auswählen"
                dialog.dismiss()
                stopPlayback()
                status.text = "Ausgewählt: ${selectedReciter?.name}"
            }
            .setNegativeButton("Abbrechen", null)
            .show()
    }

    private fun playSelectedVerse() {
        val reciter = selectedReciter ?: return
        val base = prefs.getString("audio_api_base_url", "").orEmpty().trimEnd('/')
        playButton.isEnabled = false
        status.text = "Lade Audio für Aya $verseKey …"
        executor.execute {
            try {
                val encodedKey = URLEncoder.encode(verseKey, "UTF-8")
                val json = requestJson("$base/v1/audio?verse_key=$encodedKey&recitation_id=${reciter.id}")
                val audioUrl = json.optString("audio_url", "")
                if (!audioUrl.startsWith("https://")) throw IllegalStateException("Ungültige Audio-Adresse.")
                runOnUiThread {
                    if (!isFinishing && !isDestroyed) preparePlayback(audioUrl, reciter)
                }
            } catch (error: Exception) {
                runOnUiThread {
                    playButton.isEnabled = true
                    status.text = "Audio konnte nicht geladen werden: ${error.message ?: "Unbekannter Fehler"}"
                }
            }
        }
    }

    private fun preparePlayback(audioUrl: String, reciter: Reciter) {
        stopPlayback()
        status.text = "Puffer …"
        playButton.text = "Wird geladen …"
        val media = MediaPlayer()
        player = media
        media.setOnPreparedListener {
            if (player !== it || isFinishing || isDestroyed) {
                runCatching { it.release() }
                return@setOnPreparedListener
            }
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                runCatching { it.setPlaybackParams(PlaybackParams().setSpeed(speed)) }
            }
            it.start()
            playButton.text = "Ⅱ Pause"
            playButton.isEnabled = true
            status.text = "Jetzt läuft Aya $verseKey · ${reciter.name}"
        }
        media.setOnCompletionListener {
            playButton.text = "▶ Erneut abspielen"
            status.text = "Wiedergabe beendet."
        }
        media.setOnErrorListener { _, _, _ ->
            playButton.text = "▶ Erneut versuchen"
            playButton.isEnabled = true
            status.text = "Die Audio-Wiedergabe ist fehlgeschlagen."
            true
        }
        playButton.setOnClickListener {
            val current = player
            if (current?.isPlaying == true) {
                current.pause()
                playButton.text = "▶ Fortsetzen"
                status.text = "Pausiert."
            } else if (current != null && runCatching {
                    if (current.currentPosition >= current.duration) current.seekTo(0)
                    current.start()
                }.isSuccess) {
                playButton.text = "Ⅱ Pause"
                status.text = "Wiedergabe läuft."
            } else {
                playSelectedVerse()
            }
        }
        try {
            media.setDataSource(audioUrl)
            media.prepareAsync()
        } catch (error: Exception) {
            stopPlayback()
            playButton.isEnabled = true
            playButton.text = "▶ Erneut versuchen"
            status.text = "Audio konnte nicht gestartet werden."
        }
    }

    private fun requestJson(address: String): JSONObject {
        val connection = URL(address).openConnection() as HttpURLConnection
        connection.connectTimeout = 12_000
        connection.readTimeout = 18_000
        connection.requestMethod = "GET"
        return try {
            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val body = stream.bufferedReader().use { it.readText() }
            if (code !in 200..299) {
                val message = runCatching { JSONObject(body).optString("error") }.getOrNull()
                throw IllegalStateException(message?.takeIf { it.isNotBlank() } ?: "HTTP $code")
            }
            JSONObject(body)
        } finally {
            connection.disconnect()
        }
    }

    private fun stopPlayback() {
        player?.let { media ->
            runCatching {
                if (media.isPlaying) media.stop()
                media.reset()
                media.release()
            }
        }
        player = null
        if (::playButton.isInitialized) playButton.text = "▶ Abspielen"
    }

    override fun onPause() {
        stopPlayback()
        super.onPause()
    }

    override fun onDestroy() {
        stopPlayback()
        executor.shutdownNow()
        super.onDestroy()
    }

    private fun params(top: Int = 0, bottom: Int = 0) =
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            topMargin = dp(top)
            bottomMargin = dp(bottom)
        }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
