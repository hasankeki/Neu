package app.nurwallpaper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import kotlin.math.sin
import kotlin.random.Random

/**
 * Three intentionally different visual languages for NurWallpaper.
 * All scenes are drawn locally: no remote image, account, or paid asset dependency.
 */
internal object WallpaperDesignRenderer {
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    fun draw(canvas: Canvas, design: String, phase: Float, animated: Boolean) {
        when (design) {
            "ornament" -> drawOrnament(canvas, phase, animated)
            "glass" -> drawGlass(canvas, phase, animated)
            else -> drawDreamscape(canvas, phase, animated)
        }
    }

    private fun drawDreamscape(c: Canvas, phase: Float, animated: Boolean) {
        val w = c.width.toFloat(); val h = c.height.toFloat()
        c.drawColor(Color.rgb(7, 25, 48))
        fill.shader = LinearGradient(0f, 0f, w * .2f, h,
            intArrayOf(0xFF173E63.toInt(), 0xFF102D50.toInt(), 0xFF080F25.toInt()),
            null, Shader.TileMode.CLAMP)
        c.drawRect(0f, 0f, w, h, fill); fill.shader = null

        fill.shader = RadialGradient(w * .72f, h * .25f, w * .62f,
            intArrayOf(0x665EB7E8, 0x22569AC8, 0x00000000),
            null, Shader.TileMode.CLAMP)
        c.drawRect(0f, 0f, w, h, fill); fill.shader = null

        val moonX = w * .72f
        val moonY = h * (.205f + if (animated) .006f * sin(phase * .12f) else 0f)
        fill.color = 0xFFE5F1F6.toInt()
        fill.setShadowLayer(w * .07f, 0f, 0f, 0x668ED9FF)
        c.drawCircle(moonX, moonY, w * .105f, fill)
        fill.clearShadowLayer()

        // Slow, fine stars; no fast glitter.
        val random = Random(518)
        repeat(58) { i ->
            val x = random.nextFloat() * w
            val y = random.nextFloat() * h * .62f
            val drift = if (animated) sin(phase * .10f + i) * 2.5f else 0f
            fill.color = 0xFFDCEEFF.toInt()
            fill.alpha = if (i % 7 == 0 && animated) (95 + 45 * sin(phase * .18f + i)).toInt().coerceIn(45, 145) else 95
            c.drawCircle(x + drift, y, if (i % 9 == 0) 2.1f else 1.0f, fill)
        }
        fill.alpha = 255

        // Layered distant ridges create depth behind the mosque.
        mountain(c, w, h, .57f, 0xFF274D6B.toInt(), 0f)
        mountain(c, w, h, .68f, 0xFF183652.toInt(), 1.2f)
        mountain(c, w, h, .79f, 0xFF0D2037.toInt(), 2.4f)

        // Mosque silhouette: dome, courtyard, minarets and crescent finials.
        fill.color = 0xFF081626.toInt()
        c.drawRect(0f, h * .79f, w, h, fill)
        val baseY = h * .805f
        c.drawRoundRect(w*.22f, h*.70f, w*.78f, baseY, w*.018f, w*.018f, fill)
        c.drawArc(w*.36f, h*.585f, w*.64f, h*.79f, 180f, 180f, true, fill)
        c.drawRect(w*.36f, h*.69f, w*.64f, baseY, fill)
        repeat(2) { i ->
            val x = if (i == 0) w*.18f else w*.79f
            c.drawRect(x, h*.48f, x+w*.025f, baseY, fill)
            c.drawRect(x-w*.012f, h*.48f, x+w*.037f, h*.50f, fill)
            c.drawRect(x-w*.006f, h*.43f, x+w*.031f, h*.48f, fill)
            fill.color = 0xFFB9D9E8.toInt()
            c.drawArc(x-w*.014f, h*.405f, x+w*.039f, h*.455f, 180f, 180f, false, stroke.apply { color = 0xFFB9D9E8.toInt(); strokeWidth = w*.004f })
            fill.color = 0xFF081626.toInt()
        }
        // Warm windows are tiny accents, not the main effect.
        fill.color = 0xFFE9C987.toInt()
        repeat(5) { i -> c.drawRoundRect(w*(.405f+i*.04f), h*.735f, w*(.425f+i*.04f), h*.78f, w*.01f, w*.01f, fill) }
        fill.color = 0xFF081626.toInt()
        c.drawRect(w*.49f, h*.72f, w*.51f, baseY, fill)

        // Low atmospheric light at the horizon.
        fill.shader = RadialGradient(w*.5f, h*.79f, w*.42f,
            intArrayOf(0x447FC6E6, 0x117FC6E6, 0x00000000), null, Shader.TileMode.CLAMP)
        c.drawRect(0f, h*.55f, w, h, fill); fill.shader = null
    }

    private fun mountain(c: Canvas, w: Float, h: Float, top: Float, color: Int, offset: Float) {
        val p = Path().apply {
            moveTo(0f, h*top)
            cubicTo(w*.18f, h*(top-.13f+offset*.008f), w*.25f, h*(top+.08f), w*.43f, h*(top-.035f))
            cubicTo(w*.62f, h*(top-.18f), w*.72f, h*(top+.07f), w, h*(top-.045f))
            lineTo(w, h); lineTo(0f, h); close()
        }
        fill.color = color
        c.drawPath(p, fill)
    }

    private fun drawOrnament(c: Canvas, phase: Float, animated: Boolean) {
        val w = c.width.toFloat(); val h = c.height.toFloat()
        c.drawColor(0xFFF0E8D8.toInt())
        fill.shader = LinearGradient(0f, 0f, w, h,
            intArrayOf(0xFFF9F5EA.toInt(), 0xFFE8DDC8.toInt(), 0xFFF6F0E3.toInt()),
            null, Shader.TileMode.CLAMP)
        c.drawRect(0f, 0f, w, h, fill); fill.shader = null

        // Warm paper-like illumination and quiet edge vignette.
        fill.shader = RadialGradient(w*.5f, h*.42f, w*.85f,
            intArrayOf(0x00FFFFFF, 0x22A58D69, 0x44816B4B), null, Shader.TileMode.CLAMP)
        c.drawRect(0f, 0f, w, h, fill); fill.shader = null

        stroke.color = 0x668F7958
        stroke.strokeWidth = 1.4f
        val inset = w*.055f
        c.drawRoundRect(inset, h*.10f, w-inset, h*.90f, 22f, 22f, stroke)
        c.drawRoundRect(inset+w*.018f, h*.115f, w-inset-w*.018f, h*.885f, 18f, 18f, stroke)

        // Repeating medallions / arabesque-inspired rosettes.
        val radius = w*.105f
        for (row in 0..4) for (col in 0..2) {
            val x = w*(.18f + col*.32f)
            val y = h*(.20f + row*.15f)
            drawRosette(c, x, y, radius, if ((row+col)%2==0) 0x558E7857 else 0x447C9A86)
        }
        drawRosette(c, w*.5f, h*.50f, w*.25f, 0x887F6B4E.toInt())
        drawRosette(c, w*.5f, h*.50f, w*.19f, 0x668F7958)
        // A restrained central light breathes slowly.
        fill.shader = RadialGradient(w*.5f, h*.5f, w*.42f,
            intArrayOf(0x44FFFFFF, 0x11FFFFFF, 0x00FFFFFF), null, Shader.TileMode.CLAMP)
        c.drawCircle(w*.5f, h*.5f, w*.42f, fill); fill.shader = null
        if (animated) {
            fill.color = 0x558F7958
            val pulse = 1f + .025f * sin(phase * .16f)
            c.drawCircle(w*.5f, h*.5f, w*.012f*pulse, fill)
        }
    }

    private fun drawRosette(c: Canvas, x: Float, y: Float, r: Float, color: Int) {
        stroke.color = color
        stroke.strokeWidth = 1.15f
        for (i in 0 until 8) {
            val angle = Math.toRadians(i * 45.0)
            val px = x + kotlin.math.cos(angle).toFloat() * r*.48f
            val py = y + kotlin.math.sin(angle).toFloat() * r*.48f
            c.drawCircle(px, py, r*.47f, stroke)
        }
        c.drawCircle(x, y, r*.18f, stroke)
        c.drawCircle(x, y, r*.72f, stroke)
    }

    private fun drawGlass(c: Canvas, phase: Float, animated: Boolean) {
        val w = c.width.toFloat(); val h = c.height.toFloat()
        c.drawColor(0xFF071A38.toInt())
        fill.shader = LinearGradient(0f, 0f, w, h,
            intArrayOf(0xFFB9D9F2.toInt(), 0xFF3D75B0.toInt(), 0xFF071B43.toInt()),
            null, Shader.TileMode.CLAMP)
        c.drawRect(0f, 0f, w, h, fill); fill.shader = null
        fill.shader = RadialGradient(w*.78f, h*.2f, w*.8f,
            intArrayOf(0x99FFFFFF.toInt(), 0x22D7EDFF, 0x00000000), null, Shader.TileMode.CLAMP)
        c.drawRect(0f, 0f, w, h, fill); fill.shader = null

        // Three translucent, independently moving glass ribbons.
        for (i in 0..2) {
            val drift = if (animated) sin(phase * (.10f + i*.025f) + i*1.7f) * w*.025f else 0f
            val path = Path().apply {
                moveTo(w*(.05f+i*.12f)+drift, h*1.02f)
                cubicTo(w*(.23f+i*.05f)+drift, h*.72f, w*(.20f+i*.18f)-drift, h*.42f, w*(.53f+i*.13f)+drift, h*.02f)
                cubicTo(w*(.73f+i*.08f)+drift, h*.28f, w*(.35f+i*.16f)-drift, h*.63f, w*(.93f-i*.06f)+drift, h*1.02f)
            }
            fill.shader = LinearGradient(w*.12f, 0f, w*.86f, h,
                intArrayOf(0x2269BFFF, 0xDDF4FCFF.toInt(), 0x6652A6E8, 0xCCFFFFFF.toInt()),
                null, Shader.TileMode.CLAMP)
            c.drawPath(path, fill); fill.shader = null
            stroke.color = if (i == 1) 0xFFF8FDFF.toInt() else 0xAAE5F5FF.toInt()
            stroke.strokeWidth = if (i == 1) w*.006f else w*.003f
            c.drawPath(path, stroke)
        }
        // Soft refracted highlights slide slowly along the glass.
        val glintX = w*(.5f + if (animated) .16f*sin(phase*.12f) else 0f)
        fill.shader = RadialGradient(glintX, h*.48f, w*.32f,
            intArrayOf(0x66FFFFFF, 0x18FFFFFF, 0x00FFFFFF), null, Shader.TileMode.CLAMP)
        c.drawCircle(glintX, h*.48f, w*.32f, fill); fill.shader = null
    }
}
