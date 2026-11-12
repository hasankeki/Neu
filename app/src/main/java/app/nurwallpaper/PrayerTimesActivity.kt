package app.nurwallpaper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.net.URLEncoder

class PrayerTimesActivity : Activity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var status: TextView
    private lateinit var timesContainer: LinearLayout
    private val permissionRequest = 42
    private val gold = 0xFFFFD98A.toInt()
    private val ivory = 0xFFF0E7D6.toInt()
    private val muted = 0xFFBBA77E.toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 32, 28, 24)
            setBackgroundColor(0xFF070605.toInt())
        }
        root.addView(TextView(this).apply {
            text = "Gebetszeiten"
            textSize = 29f
            setTextColor(gold)
            gravity = Gravity.CENTER
        })
        status = TextView(this).apply {
            text = "Dein Standort wird gesucht …"
            textSize = 14f
            setTextColor(muted)
            gravity = Gravity.CENTER
        }
        root.addView(status)
        timesContainer = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        root.addView(timesContainer)
        root.addView(Button(this).apply {
            text = "Standort aktualisieren"
            isAllCaps = false
            setOnClickListener { findLocation() }
        })
        root.addView(Button(this).apply {
            text = "Zurück"
            isAllCaps = false
            setOnClickListener { finish() }
        })
        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        if (::locationManager.isInitialized && hasLocationPermission()) findLocation()
    }

    override fun onPause() {
        if (::locationManager.isInitialized) locationManager.removeUpdates(this)
        super.onPause()
    }

    private fun hasLocationPermission(): Boolean =
        checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun findLocation() {
        if (!hasLocationPermission()) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                permissionRequest
            )
            return
        }
        status.text = "Standort wird ermittelt …"
        val providers = locationManager.getProviders(true)
        var best: Location? = null
        providers.forEach { provider ->
            val candidate = runCatching { locationManager.getLastKnownLocation(provider) }.getOrNull()
            if (candidate != null && (best == null || candidate.accuracy < best!!.accuracy)) best = candidate
        }
        if (best != null) {
            loadPrayerTimes(best!!)
            val provider = when {
                providers.contains(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                providers.contains(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                else -> null
            }
            if (provider != null) runCatching { locationManager.requestLocationUpdates(provider, 1000L, 100f, this) }
        } else {
            val provider = when {
                providers.contains(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                providers.contains(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                else -> null
            }
            if (provider == null) status.text = "Bitte aktiviere die Standortdienste."
            else runCatching { locationManager.requestLocationUpdates(provider, 1000L, 0f, this) }
                .onFailure { status.text = "Standort konnte nicht angefordert werden." }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest && grantResults.any { it == PackageManager.PERMISSION_GRANTED }) findLocation()
        else status.text = "Ohne Standortfreigabe können keine Gebetszeiten geladen werden."
    }

    override fun onLocationChanged(location: Location) {
        locationManager.removeUpdates(this)
        loadPrayerTimes(location)
    }

    private fun loadPrayerTimes(location: Location) {
        status.text = "Gebetszeiten für deinen Standort werden geladen …"
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())
        val preferences = getSharedPreferences("nurwallpaper_settings", MODE_PRIVATE)
        val method = preferences.getInt("prayer_method", 13)
        val school = preferences.getInt("prayer_school", 1)
        val address = "https://api.aladhan.com/v1/timings/$date" +
            "?latitude=${location.latitude}&longitude=${location.longitude}" +
            "&method=$method&school=$school&latitudeAdjustmentMethod=3&timezonestring=" + URLEncoder.encode(TimeZone.getDefault().id, "UTF-8")
        Thread {
            var connection: HttpURLConnection? = null
            try {
                connection = (URL(address).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 10000
                    readTimeout = 10000
                    setRequestProperty("Accept", "application/json")
                }
                if (connection.responseCode !in 200..299) error("HTTP ${connection.responseCode}")
                val body = connection.inputStream.bufferedReader().use { it.readText() }
                val timings = JSONObject(body).getJSONObject("data").getJSONObject("timings")
                val keys = listOf(
                    "Fajr" to "Fadschr",
                    "Sunrise" to "Sonnenaufgang",
                    "Dhuhr" to "Dhuhr",
                    "Asr" to "Asr",
                    "Maghrib" to "Maghrib",
                    "Isha" to "Ischa"
                )
                val values = keys.map { (key, label) -> label to timings.getString(key).substringBefore(" ") }
                runOnUiThread {
                    timesContainer.removeAllViews()
                    values.forEach { (label, time) ->
                        val row = LinearLayout(this).apply {
                            orientation = LinearLayout.HORIZONTAL
                            setPadding(18, 13, 18, 13)
                            setBackgroundColor(0xFF18130C.toInt())
                        }
                        row.addView(TextView(this).apply {
                            text = label
                            textSize = 17f
                            setTextColor(ivory)
                        }, LinearLayout.LayoutParams(0, -2, 1f))
                        row.addView(TextView(this).apply {
                            text = time
                            textSize = 18f
                            setTextColor(gold)
                        })
                        timesContainer.addView(row, LinearLayout.LayoutParams(-1, -2).apply {
                            bottomMargin = (6 * resources.displayMetrics.density).toInt()
                        })
                    }
                    val methodLabel = when (method) { 3 -> "Muslim World League"; 2 -> "ISNA"; 5 -> "Ägypten"; 1 -> "Karachi"; else -> "Diyanet" }
                    val schoolLabel = if (school == 1) "Hanafi" else "Shafi"
                    status.text = "$methodLabel · $schoolLabel · Standortgenauigkeit ±${location.accuracy.toInt()} m"
                    timesContainer.addView(TextView(this).apply {
                        text = "Berechnung über AlAdhan. Dein Standort wird zur Berechnung übermittelt. Zeiten können vom örtlichen Moschee-Kalender abweichen."
                        textSize = 12f
                        setTextColor(muted)
                        setPadding(2, 12, 2, 4)
                    })
                }
            } catch (error: Exception) {
                runOnUiThread {
                    status.text = "Gebetszeiten konnten nicht geladen werden. Bitte Internetverbindung prüfen und erneut versuchen."
                }
            } finally {
                connection?.disconnect()
            }
        }.start()
    }

    override fun onProviderEnabled(provider: String) = Unit
    override fun onProviderDisabled(provider: String) {
        runOnUiThread { status.text = "Standortdienst deaktiviert. Bitte Standort aktivieren." }
    }
}
