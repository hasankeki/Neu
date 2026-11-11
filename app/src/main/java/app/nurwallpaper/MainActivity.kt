package app.nurwallpaper

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
        }

        val title = TextView(this).apply {
            text = getString(R.string.app_name)
            textSize = 28f
            gravity = Gravity.CENTER
        }

        val description = TextView(this).apply {
            text = "Dein ruhiger Bildschirm. Inhalte und Einstellungen folgen im nächsten Entwicklungsschritt."
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 28)
        }

        val chooseWallpaper = Button(this).apply {
            text = "Live-Wallpaper auswählen"
            setOnClickListener {
                val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                    putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        ComponentName(this@MainActivity, NurWallpaperService::class.java)
                    )
                }
                startActivity(intent)
            }
        }

        content.addView(title)
        content.addView(description)
        content.addView(chooseWallpaper)
        setContentView(content)
    }
}
