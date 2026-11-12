package app.nurwallpaper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.view.Surface
import android.view.View
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.math.*

class QiblaActivity : Activity(), SensorEventListener, LocationListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var locationManager: LocationManager
    private lateinit var compass: CompassView
    private lateinit var status: TextView
    private lateinit var directionText: TextView
    private var qiblaBearing: Float? = null
    private var azimuth = 0f
    private val permissionRequest = 41

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
            setBackgroundColor(Color.rgb(7, 6, 5))
        }
        val title = TextView(this).apply {
            text = "Qibla"
            textSize = 30f
            setTextColor(0xFFFFD98A.toInt())
            gravity = Gravity.CENTER
        }
        directionText = TextView(this).apply {
            text = "Richtung wird bestimmt …"
            textSize = 17f
            setTextColor(0xFFF0E7D6.toInt())
            gravity = Gravity.CENTER
        }
        compass = CompassView(this)
        status = TextView(this).apply {
            text = "Für die Richtung wird dein Standort benötigt."
            textSize = 14f
            setTextColor(0xFFBBA77E.toInt())
            gravity = Gravity.CENTER
        }
        val locate = Button(this).apply {
            text = "Standort bestimmen"
            isAllCaps = false
            setOnClickListener { obtainLocation() }
        }
        val back = Button(this).apply {
            text = "Zurück"
            isAllCaps = false
            setOnClickListener { finish() }
        }
        root.addView(title)
        root.addView(compass, LinearLayout.LayoutParams(-1, 0, 1f))
        root.addView(directionText)
        root.addView(status)
        root.addView(locate)
        root.addView(back)
        setContentView(root)
        obtainLocation()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        } ?: run {
            status.text = "Kompasssensor auf diesem Gerät nicht verfügbar."
        }
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        if (::locationManager.isInitialized) locationManager.removeUpdates(this)
        super.onPause()
    }

    private fun obtainLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                permissionRequest)
            return
        }
        status.text = "Standort wird gesucht …"
        val providers = locationManager.getProviders(true)
        var best: Location? = null
        for (provider in providers) {
            val candidate = runCatching { locationManager.getLastKnownLocation(provider) }.getOrNull()
            if (candidate != null && (best == null || candidate.accuracy < best!!.accuracy)) best = candidate
        }
        if (best != null) {
            updateQibla(best!!)
            val provider = if (providers.contains(LocationManager.GPS_PROVIDER)) LocationManager.GPS_PROVIDER else LocationManager.NETWORK_PROVIDER
            runCatching { locationManager.requestLocationUpdates(provider, 1500L, 10f, this) }
        } else {
            status.text = "Warte auf einen aktuellen Standort. Bitte Standortdienste aktivieren."
            val provider = when {
                providers.contains(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                providers.contains(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                else -> null
            }
            if (provider != null) runCatching { locationManager.requestLocationUpdates(provider, 1000L, 0f, this) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest && grantResults.any { it == PackageManager.PERMISSION_GRANTED }) obtainLocation()
        else status.text = "Ohne Standortfreigabe kann die Qibla-Richtung nicht berechnet werden."
    }

    private fun updateQibla(location: Location) {
        // Great-circle initial bearing from the current position to the Kaaba in Makkah.
        val lat1 = Math.toRadians(location.latitude)
        val lon1 = Math.toRadians(location.longitude)
        val lat2 = Math.toRadians(21.4225)
        val lon2 = Math.toRadians(39.8262)
        val dLon = lon2 - lon1
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
        qiblaBearing = ((Math.toDegrees(atan2(y, x)) + 360.0) % 360.0).toFloat()
        status.text = "Standortgenauigkeit: ±${location.accuracy.toInt()} m"
        updateDisplay()
    }

    override fun onLocationChanged(location: Location) {
        updateQibla(location)
        locationManager.removeUpdates(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ROTATION_VECTOR) return
        val matrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(matrix, event.values)
        val adjusted = FloatArray(9)
        val rotation = windowManager.defaultDisplay.rotation
        val axisX = if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) SensorManager.AXIS_X else SensorManager.AXIS_Y
        val axisY = if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) SensorManager.AXIS_Y else SensorManager.AXIS_MINUS_X
        SensorManager.remapCoordinateSystem(matrix, axisX, axisY, adjusted)
        val orientation = FloatArray(3)
        SensorManager.getOrientation(adjusted, orientation)
        azimuth = ((Math.toDegrees(orientation[0].toDouble()) + 360.0) % 360.0).toFloat()
        updateDisplay()
    }

    private fun updateDisplay() {
        val bearing = qiblaBearing ?: return
        val relative = (bearing - azimuth + 360f) % 360f
        compass.setDirection(relative)
        directionText.text = "Qibla: ${bearing.roundToInt()}°  ·  ${relative.roundToInt()}° von deiner Ausrichtung"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private class CompassView(context: Context) : View(context) {
        private val ring = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = 0xFFE2B453.toInt()
        }
        private val needle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFFFD98A.toInt()
            style = Paint.Style.FILL
        }
        private val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFF0E7D6.toInt()
            textSize = 22f
            textAlign = Paint.Align.CENTER
        }
        private var direction = 0f
        fun setDirection(value: Float) { direction = value; invalidate() }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val cx = width / 2f
            val cy = height / 2f
            val radius = min(width, height) * .34f
            canvas.drawCircle(cx, cy, radius, ring)
            canvas.drawText("N", cx, cy - radius - 18f, text)
            canvas.drawText("S", cx, cy + radius + 32f, text)
            canvas.drawText("W", cx - radius - 22f, cy + 8f, text)
            canvas.drawText("O", cx + radius + 22f, cy + 8f, text)
            canvas.save()
            canvas.rotate(direction, cx, cy)
            val arrow = android.graphics.Path().apply {
                moveTo(cx, cy - radius * .78f)
                lineTo(cx - radius * .13f, cy + radius * .12f)
                lineTo(cx, cy + radius * .02f)
                lineTo(cx + radius * .13f, cy + radius * .12f)
                close()
            }
            canvas.drawPath(arrow, needle)
            canvas.drawCircle(cx, cy, 7f, ring)
            canvas.restore()
        }
    }
}
