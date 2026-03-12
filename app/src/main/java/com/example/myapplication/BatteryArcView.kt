package com.example.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class BatteryArcView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var health: Float = 85f

    // Paints
    private val bgArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        color = Color.parseColor("#1E2832")
    }

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        color = Color.parseColor("#0D3B4A")
    }

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = Color.WHITE
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val subtextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = Color.parseColor("#8899AA")
    }

    private val oval = RectF()

    fun setHealth(value: Float) {
        health = value.coerceIn(0f, 100f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val cx = w / 2f
        val cy = h / 2f
        val size = min(w, h)
        val strokeW = size * 0.07f
        val radius = size / 2f - strokeW

        bgArcPaint.strokeWidth  = strokeW
        trackPaint.strokeWidth  = strokeW * 0.5f
        arcPaint.strokeWidth    = strokeW
        glowPaint.strokeWidth   = strokeW * 1.6f

        // Màu theo health
        val color = when {
            health >= 80 -> Color.parseColor("#00E676")
            health >= 60 -> Color.parseColor("#FFAB40")
            health >= 40 -> Color.parseColor("#FF7043")
            else         -> Color.parseColor("#EF5350")
        }
        val colorDim = Color.argb(60,
            Color.red(color), Color.green(color), Color.blue(color))

        arcPaint.color  = color
        dotPaint.color  = color
        glowPaint.color = colorDim

        oval.set(cx - radius, cy - radius, cx + radius, cy + radius)

        val startAngle = 150f
        val sweepTotal = 240f

        // Track (nền cung)
        trackPaint.color = Color.parseColor("#12232E")
        canvas.drawArc(oval, startAngle, sweepTotal, false, trackPaint)

        // Glow layer
        val glowOval = RectF(
            cx - radius, cy - radius, cx + radius, cy + radius
        )
        val sweepArc = sweepTotal * health / 100f
        canvas.drawArc(glowOval, startAngle, sweepArc, false, glowPaint)

        // Main arc
        canvas.drawArc(oval, startAngle, sweepArc, false, arcPaint)

        // Dot at end of arc
        val endAngleDeg = startAngle + sweepArc
        val endRad = Math.toRadians(endAngleDeg.toDouble())
        val dotX = (cx + radius * cos(endRad)).toFloat()
        val dotY = (cy + radius * sin(endRad)).toFloat()
        canvas.drawCircle(dotX, dotY, strokeW * 0.55f, dotPaint)

        // Tick marks (giống AccuBattery)
        drawTicks(canvas, cx, cy, radius, strokeW, sweepTotal, startAngle, color)

        // Center text
        textPaint.textSize = size * 0.22f
        canvas.drawText("${health.toInt()}%", cx, cy + textPaint.textSize * 0.35f, textPaint)

        subtextPaint.textSize = size * 0.09f
        canvas.drawText("Sức khoẻ pin", cx, cy + textPaint.textSize * 0.35f + subtextPaint.textSize * 1.3f, subtextPaint)
    }

    private fun drawTicks(
        canvas: Canvas, cx: Float, cy: Float,
        radius: Float, strokeW: Float,
        sweepTotal: Float, startAngle: Float,
        activeColor: Int
    ) {
        val tickCount = 20
        val outerR = radius + strokeW * 0.1f
        val innerR = radius - strokeW * 0.5f
        val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeWidth = strokeW * 0.12f
        }
        for (i in 0..tickCount) {
            val frac  = i.toFloat() / tickCount
            val angle = startAngle + sweepTotal * frac
            val rad   = Math.toRadians(angle.toDouble())
            val x1 = (cx + outerR * cos(rad)).toFloat()
            val y1 = (cy + outerR * sin(rad)).toFloat()
            val x2 = (cx + innerR * cos(rad)).toFloat()
            val y2 = (cy + innerR * sin(rad)).toFloat()
            tickPaint.color = if (frac <= health / 100f) activeColor else Color.parseColor("#1E2832")
            canvas.drawLine(x1, y1, x2, y2, tickPaint)
        }
    }
}