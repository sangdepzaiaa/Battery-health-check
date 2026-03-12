package com.example.myapplication.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.div
import kotlin.math.sin
import kotlin.text.toFloat
import kotlin.times


class BatteryWaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paintWave1 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintWave2 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintWater = Paint(Paint.ANTI_ALIAS_FLAG)

    private val path = Path()

    private var waveShift = 0f
    private var waterLevel = 0.5f
    private var waveOffset = 0f
    private var amplitude = 50f
    private var waveHeightFactor = 1f



    private var animator: ValueAnimator? = null

    init {

        // Sóng 1 (cao nhất - nhạt)
        paintWave1.color = Color.parseColor("#2237C186")
        paintWave1.style = Paint.Style.FILL

        // Sóng 2 (ở giữa)
        paintWave2.color = Color.parseColor("#5537C186")
        paintWave2.style = Paint.Style.FILL

        // Nước chính (đậm)
        paintWater.color = Color.parseColor("#37C186")
        paintWater.style = Paint.Style.FILL

       // startAnimation()
    }

    fun setProgress(percent: Int) {
        waterLevel = percent / 100f
        invalidate()
    }



    private fun startAnimation() {

        if (animator?.isRunning == true) return

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 12L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()

            addUpdateListener {
                waveOffset += 0.4f
                amplitude = (50f + 15f * sin(waveOffset * Math.PI / 180f)).toFloat()
                invalidate()
            }

            start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        animator = null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // vẽ từ dưới lên
        drawWave(canvas, paintWater, 25f, Math.PI.toFloat(), 0f)

        drawWave(canvas, paintWave2, 30f, ((Math.PI.toFloat())*0.8).toFloat(), -15f)

        drawWave(canvas, paintWave1, 35f, Math.PI.toFloat(), -35f)
    }


    private fun drawWave(
        canvas: Canvas,
        paint: Paint,
        amplitude: Float,
        phase: Float,
        offsetY: Float
    ) {
        path.reset()

        val width = width.toFloat()
        val height = height.toFloat()

        val waterY = height * (1 - waterLevel) + offsetY

        val frequency = 2 * Math.PI / width

        path.moveTo(0f, height)

        for (x in 0..width.toInt()) {
            val y = (amplitude * waveHeightFactor *
                    sin(frequency * x + phase + waveOffset * Math.PI / 180f)  // Add waveOffset for horizontal shift
                    + waterY).toFloat()

            path.lineTo(x.toFloat(), y)
        }

        path.lineTo(width, height)
        path.close()

        canvas.drawPath(path, paint)
    }

}