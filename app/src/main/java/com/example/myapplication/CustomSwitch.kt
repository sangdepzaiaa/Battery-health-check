package com.example.myapplication

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class IOSSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var isChecked = false

    private val paintTrack = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintThumb = Paint(Paint.ANTI_ALIAS_FLAG)

    private var thumbPosition = 0f

    private val trackRect = RectF()

    private val offColor = Color.parseColor("#C7CED4")
    private val onColor = Color.parseColor("#34C759")

    init {
        paintThumb.color = Color.WHITE
        elevation = dp(2f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = dp(52f).toInt()
        val h = dp(32f).toInt()
        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = height / 2f

        trackRect.set(0f, 0f, width.toFloat(), height.toFloat())

        paintTrack.color = if (isChecked) onColor else offColor
        canvas.drawRoundRect(trackRect, radius, radius, paintTrack)

        val thumbRadius = height * 0.42f

        val minX = radius
        val maxX = width - radius

        val cx = minX + (maxX - minX) * thumbPosition
        val cy = height / 2f

        canvas.drawCircle(cx, cy, thumbRadius, paintThumb)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            toggle()
        }
        return true
    }

    private fun toggle() {
        isChecked = !isChecked
        animateThumb()
    }

    private fun animateThumb() {

        val start = thumbPosition
        val end = if (isChecked) 1f else 0f

        ValueAnimator.ofFloat(start, end).apply {

            duration = 180

            addUpdateListener {
                thumbPosition = it.animatedValue as Float
                invalidate()
            }

            start()
        }
    }

    fun setChecked(value: Boolean) {
        isChecked = value
        thumbPosition = if (value) 1f else 0f
        invalidate()
    }

    fun isChecked(): Boolean {
        return isChecked
    }

    private fun dp(v: Float): Float {
        return v * resources.displayMetrics.density
    }
}