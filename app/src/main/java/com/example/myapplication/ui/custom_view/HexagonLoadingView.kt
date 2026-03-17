package com.example.myapplication.ui.custom_view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class HexagonLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF18C29C.toInt()
        style = Paint.Style.FILL
    }

    private var rotation = 0f
    private var wave = 0f

    private val animator = ValueAnimator.ofFloat(0f, 360f).apply {
        duration = 5000
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            rotation = it.animatedValue as Float
            wave = rotation
            invalidate()
        }
    }

    init {
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f

        canvas.save()
        canvas.rotate(rotation * 0.2f, cx, cy)

        drawHexLayer(canvas, cx, cy, width * 0.20f, 16)
        drawHexLayer(canvas, cx, cy, width * 0.30f, 24)
        drawHexLayer(canvas, cx, cy, width * 0.40f, 36)

        canvas.restore()
    }

    private fun drawHexLayer(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        dotCount: Int
    ) {

        val dotRadius = 5f

        for (i in 0 until dotCount) {

            val angle = Math.toRadians((360.0 / dotCount) * i)

            val x = cx + radius * cos(angle)
            val y = cy + radius * sin(angle)

            val alphaWave = ((sin(Math.toRadians((wave + i * 10).toDouble())) + 1) / 2)

            dotPaint.alpha = (80 + alphaWave * 175).toInt()

            canvas.drawCircle(x.toFloat(), y.toFloat(), dotRadius, dotPaint)
        }
    }
}