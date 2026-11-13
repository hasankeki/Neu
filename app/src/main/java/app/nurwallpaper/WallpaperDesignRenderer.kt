package app.nurwallpaper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import kotlin.math.cos
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
    private val glow = Paint(Paint.ANTI_ALIAS_FLAG)

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
        mountain(c, w, h, .57f, 0xFF274D6B.toInt(), 0f)
        mountain(c, w, h, .68f, 0xFF183652.toInt(), 1.2f)
        mountain(c, w, h, .79f, 0xFF0D2037.toInt(), 2.4f)
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
        }
        fill.color = 0xFFE9C987.toInt()
        repeat(5) { i -> c.drawRoundRect(w*(.405f+i*.04f), h*.735f, w*(.425f+i*.04f), h*.78f, w*.01f, w*.01f, fill) }
        fill.color = 0xFF081626.toInt()
        c.drawRect(w*.49f, h*.72f, w*.51f, baseY, fill)
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

    /**
     * Emerald Prestige: an eight-fold Islamic star lattice on a deep jade field.
     * The moving light is sampled directly from each ornament path, so the pattern
     * itself appears to emit and carry the light rather than having a generic sweep.
     */
    private fun drawOrnament(c: Canvas, phase: Float, animated: Boolean) {
        val w = c.width.toFloat(); val h = c.height.toFloat()
        val scale = w.coerceAtMost(h)
        val pulsePhase = if (animated) phase else 0f

        c.drawColor(0xFF031B17.toInt())
        fill.shader = LinearGradient(0f, 0f, w, h,
            intArrayOf(0xFF062D25.toInt(), 0xFF0B5142.toInt(), 0xFF031A17.toInt()),
            floatArrayOf(0f, .48f, 1f), Shader.TileMode.CLAMP)
        c.drawRect(0f, 0f, w, h, fill); fill.shader = null

        // A restrained emerald aura gives the center depth without washing out the art.
        fill.shader = RadialGradient(w*.5f, h*.46f, scale*.78f,
            intArrayOf(0x443FC9A0, 0x1D087D62, 0x00000000),
            null, Shader.TileMode.CLAMP)
        c.drawRect(0f, 0f, w, h, fill); fill.shader = null

        // Dark inset frame with fine champagne-gold double rules.
        val inset = w*.045f
        stroke.color = 0x887FBD9D.toInt()
        stroke.strokeWidth = scale*.0022f
        c.drawRoundRect(inset, h*.075f, w-inset, h*.925f, scale*.025f, scale*.025f, stroke)
        stroke.color = 0x66D7B978
        stroke.strokeWidth = scale*.0011f
        c.drawRoundRect(inset+w*.014f, h*.09f, w-inset-w*.014f, h*.91f, scale*.019f, scale*.019f, stroke)

        // Repeating eight-point stars create a continuous, tile-like ornamental field.
        val tileRadius = scale*.092f
        for (row in -1..5) for (col in -1..3) {
            val x = w*(.02f + col*.25f + if (row % 2 == 0) 0f else .125f)
            val y = h*(.105f + row*.16f)
            drawEmeraldStar(c, x, y, tileRadius, pulsePhase, animated, (row*5+col+32))
        }

        // Large central medallion: layered geometry, raised gold edges and a soft core.
        val cx = w*.5f
        val cy = h*.5f
        val heroRadius = scale*.245f
        drawEmeraldStar(c, cx, cy, heroRadius, pulsePhase*0.72f, animated, 777)
        stroke.color = 0xB8D8B36F.toInt()
        stroke.strokeWidth = scale*.0024f
        c.drawCircle(cx, cy, heroRadius*1.12f, stroke)
        stroke.color = 0x5587C6A4
        stroke.strokeWidth = scale*.0015f
        c.drawCircle(cx, cy, heroRadius*1.20f, stroke)

        // Gentle breathing light remains centered in the geometry.
        val breath = if (animated) .5f + .5f*sin(phase*.42f) else .45f
        fill.shader = RadialGradient(cx, cy, heroRadius*.72f,
            intArrayOf((0x22 + breath*0x20).toInt().shl(24) or 0x00D9B978,
                0x160B9C77, 0x00000000), null, Shader.TileMode.CLAMP)
        c.drawCircle(cx, cy, heroRadius*.72f, fill); fill.shader = null

        // Sparse gold dust only at the frame edges; the pattern remains the hero.
        val random = Random(9217)
        repeat(34) { i ->
            val x = random.nextFloat()*w
            val y = random.nextFloat()*h
            val edgeDistance = kotlin.math.abs(x-cx)/w
            if (edgeDistance > .34f) {
                val twinkle = if (animated) .45f + .35f*sin(phase*.55f+i) else .55f
                fill.color = 0xFFE9D09A.toInt()
                fill.alpha = (twinkle*105f).toInt().coerceIn(20, 100)
                c.drawCircle(x, y, if (i%6==0) 1.8f else 1.0f, fill)
            }
        }
        fill.alpha = 255
    }

    private fun drawEmeraldStar(
        c: Canvas, x: Float, y: Float, radius: Float,
        phase: Float, animated: Boolean, seed: Int
    ) {
        val inner = radius*.48f
        val outerPath = Path()
        val innerPath = Path()
        for (i in 0 until 16) {
            val angle = Math.PI*(i*22.5-90.0)/180.0
            val r = if (i%2==0) radius else inner
            val px = x + cos(angle).toFloat()*r
            val py = y + sin(angle).toFloat()*r
            if (i==0) outerPath.moveTo(px,py) else outerPath.lineTo(px,py)
        }
        outerPath.close()

        // Layered fills create a subtle bevel, not a flat outlined sticker.
        fill.shader = LinearGradient(x-radius, y-radius, x+radius, y+radius,
            intArrayOf(0xCC1D8065.toInt(), 0xDD084536.toInt(), 0xEE031F19.toInt()),
            null, Shader.TileMode.CLAMP)
        c.drawPath(outerPath, fill); fill.shader = null

        stroke.color = 0xB8D7B36F.toInt()
        stroke.strokeWidth = radius*.018f
        c.drawPath(outerPath, stroke)
        stroke.color = 0x6686C6A5
        stroke.strokeWidth = radius*.009f
        c.drawCircle(x, y, radius*.82f, stroke)

        // Eight-fold radial lattice and interlaced inner star.
        val lattice = Path()
        for (i in 0 until 8) {
            val a = Math.PI*(i*45.0-90.0)/180.0
            val b = Math.PI*((i*45.0+22.5)-90.0)/180.0
            val ax = x+cos(a).toFloat()*radius*.82f
            val ay = y+sin(a).toFloat()*radius*.82f
            val bx = x+cos(b).toFloat()*radius*.36f
            val by = y+sin(b).toFloat()*radius*.36f
            lattice.moveTo(ax,ay); lattice.lineTo(bx,by)
            val oppositeX = x-cos(a).toFloat()*radius*.82f
            val oppositeY = y-sin(a).toFloat()*radius*.82f
            lattice.moveTo(oppositeX,oppositeY); lattice.lineTo(bx,by)
        }
        stroke.color = 0x997FC6A5.toInt()
        stroke.strokeWidth = radius*.012f
        c.drawPath(lattice, stroke)

        // The highlight bead travels ON the star's actual geometry.
        if (animated && radius > 4f) {
            val measure = PathMeasure(outerPath, true)
            val position = FloatArray(2)
            val distance = ((phase*.13f + seed*.071f)%1f)*measure.length
            if (measure.getPosTan(distance, position, null)) {
                val px = position[0]; val py = position[1]
                glow.shader = RadialGradient(px, py, radius*.34f,
                    intArrayOf(0xFFFFE7A8.toInt(), 0x99E7C477.toInt(), 0x00000000),
                    null, Shader.TileMode.CLAMP)
                c.drawCircle(px, py, radius*.34f, glow)
                glow.shader = null
                fill.color = 0xFFFFE8B0.toInt()
                c.drawCircle(px, py, radius*.025f, fill)
            }
        }

        // A slow, narrow glint passes through the central rosette.
        val coreRadius = radius*.19f
        fill.shader = RadialGradient(x, y, coreRadius*2.4f,
            intArrayOf(0x44F8E2B4, 0x1874D4AE, 0x00000000),
            null, Shader.TileMode.CLAMP)
        c.drawCircle(x, y, coreRadius*2.4f, fill); fill.shader = null
        fill.color = 0xFFD8B878.toInt()
        c.drawCircle(x, y, coreRadius*.12f, fill)
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
        val glintX = w*(.5f + if (animated) .16f*sin(phase*.12f) else 0f)
        fill.shader = RadialGradient(glintX, h*.48f, w*.32f,
            intArrayOf(0x66FFFFFF, 0x18FFFFFF, 0x00FFFFFF), null, Shader.TileMode.CLAMP)
        c.drawCircle(glintX, h*.48f, w*.32f, fill); fill.shader = null
    }
}
