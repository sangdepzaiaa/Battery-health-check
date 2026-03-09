package com.example.myapplication.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.sin

class BatteryWaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val wavePaint1 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val wavePaint2 = Paint(Paint.ANTI_ALIAS_FLAG)
    private val wavePaint3 = Paint(Paint.ANTI_ALIAS_FLAG)

    private val wavePath = Path()

    private var waveShift = 0f
    private var waterLevel = 0.5f

    init {

        wavePaint1.color = Color.parseColor("#3337C186")
        wavePaint1.style = Paint.Style.FILL

        wavePaint2.color = Color.parseColor("#3337C186")
        wavePaint2.style = Paint.Style.FILL

        wavePaint3.color = Color.parseColor("#37C186")
        wavePaint3.style = Paint.Style.FILL

        startAnimation()
    }

    fun setProgress(percent: Int) {
        waterLevel = percent / 100f
        invalidate()
    }

     fun startAnimation() {

        val animator = ValueAnimator.ofFloat(0f, (2 * Math.PI).toFloat())

        animator.duration = 2000
        animator.repeatCount = ValueAnimator.INFINITE

        animator.addUpdateListener {
            waveShift = it.animatedValue as Float
            invalidate()
        }

        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawWave(canvas, wavePaint1, 1f, 0f)
        drawWave(canvas, wavePaint2, 1.3f, 40f)
        drawWave(canvas, wavePaint3, 1.6f, 80f)
    }

    private fun drawWave(canvas: Canvas, paint: Paint, amplitudeScale: Float, phase: Float) {

        wavePath.reset()

        val width = width.toFloat()
        val height = height.toFloat()

        val waterY = height * (1 - waterLevel)

        val amplitude = 25f * amplitudeScale
        val frequency = 2 * Math.PI / width

        wavePath.moveTo(0f, height)

        for (x in 0..width.toInt()) {

            val y = (amplitude *
                    sin(frequency * x + waveShift + phase) +
                    waterY).toFloat()

            wavePath.lineTo(x.toFloat(), y)
        }

        wavePath.lineTo(width, height)
        wavePath.close()

        canvas.drawPath(wavePath, paint)
    }
}