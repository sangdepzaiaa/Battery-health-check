package com.example.myapplication.ui.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.myapplication.R
import kotlin.math.min

class BatteryHealthView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var labelText = context.getString(R.string.battery_health)

    private var percent = 75f

    private val strokeWidth = 40f

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#EF5350")
        strokeWidth = this@BatteryHealthView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val greenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#38B77B")
        strokeWidth = this@BatteryHealthView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val redPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#F24E4E")
        strokeWidth = this@BatteryHealthView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 80f + android.R.attr.radius * 0.85f
        color = Color.parseColor("#38B77B")
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 36f
        color = Color.parseColor("#37474F")
        textAlign = Paint.Align.CENTER
    }

    fun setPercent(value: Float) {
        percent = value.coerceIn(0f, 100f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = min(width, height)
        val radius = size / 2f - strokeWidth

        val cx = width / 2f
        val cy = height / 2f

        val rect = RectF(
            cx - radius,
            cy - radius,
            cx + radius,
            cy + radius
        )

        val startAngle = 135f
        val totalAngle = 270f

        canvas.drawArc(rect, startAngle, totalAngle, false, bgPaint)

        val greenSweep = percent / 100f * totalAngle
        val redSweep = totalAngle - greenSweep

// phần xanh (pin còn tốt)
        canvas.drawArc(rect, startAngle, greenSweep, false, greenPaint)

// phần đỏ (pin đã hao)
        canvas.drawArc(rect, startAngle + greenSweep, redSweep, false, redPaint)

        canvas.drawText(
            "${percent.toInt()}%",
            cx,
            cy + 20f,
            textPaint
        )

        canvas.drawText(
            context.getString(R.string.battery_health),
            cx,
            cy + radius * 0.85f,
            labelPaint
        )
    }

    init {

        attrs?.let {

            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.BatteryHealthView
            )

            textPaint.textSize = typedArray.getDimension(
                R.styleable.BatteryHealthView_percentTextSize,
                80f
            )

            textPaint.color =
                if (percent > 20) Color.parseColor("#38B77B")
                else Color.parseColor("#F24E4E")

            labelPaint.textSize = typedArray.getDimension(
                R.styleable.BatteryHealthView_labelTextSize,
                36f
            )

            labelPaint.color = typedArray.getColor(
                R.styleable.BatteryHealthView_labelTextColor,
                Color.DKGRAY
            )

            labelText = typedArray.getString(
                R.styleable.BatteryHealthView_labelText
            ) ?: "Battery health"

            val fontId = typedArray.getResourceId(
                R.styleable.BatteryHealthView_labelFont,
                -1
            )

            if (fontId != -1) {
                labelPaint.typeface = ResourcesCompat.getFont(context, fontId)
            }

            typedArray.recycle()
        }
    }
}