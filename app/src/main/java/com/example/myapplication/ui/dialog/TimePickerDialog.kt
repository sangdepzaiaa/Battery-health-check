package com.example.myapplication.ui.dialog

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.base.BaseDialog
import com.example.myapplication.databinding.LayoutDialogSetTimeBinding

class TimePickerDialog(
    context: Context,
    private val onTimeSelected: (Int, Int) -> Unit
) : BaseDialog<LayoutDialogSetTimeBinding>(context) {

    override fun getViewBinding(): LayoutDialogSetTimeBinding {
        return LayoutDialogSetTimeBinding.inflate(LayoutInflater.from(context))
    }

    override fun initViews() {
        setupPicker(dBinding.txvHour, 0,23)
        setupPicker(dBinding.txvMinute,0, 59)

        dBinding.btnSetInterval.setOnClickListener {
            val hour = dBinding.txvHour.value
            val minute = dBinding.txvMinute.value
            onTimeSelected(hour, minute)
            dismiss()
        }
    }

    private fun setupPicker(picker: NumberPicker, min: Int, max: Int) {

        picker.minValue = min
        picker.maxValue = max

        picker.wrapSelectorWheel = true
        picker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        // format 01 02 03
        picker.setFormatter { value ->
            String.format("%02d", value)
        }

        // ẩn divider
        try {
            val field = NumberPicker::class.java.getDeclaredField("mSelectionDivider")
            field.isAccessible = true
            field.set(picker, null)
        } catch (e: Exception) {}

        updatePickerTextColor(picker)

        picker.setOnValueChangedListener { _, _, _ ->
            updatePickerTextColor(picker)
        }

        picker.setOnScrollListener { _, _ ->
            picker.invalidate()
        }
    }

    private fun updatePickerTextColor(picker: NumberPicker) {

        for (i in 0 until picker.childCount) {

            val child = picker.getChildAt(i)

            if (child is EditText) {

                child.textSize = 16f
                child.setTextColor(
                    ContextCompat.getColor(context, R.color.white)
                )
            }
        }

        try {

            val field =
                NumberPicker::class.java.getDeclaredField("mSelectorWheelPaint")

            field.isAccessible = true

            val paint = field.get(picker) as Paint

            paint.color =
                ContextCompat.getColor(context, R.color.color_B0BEC5)

            picker.invalidate()

        } catch (e: Exception) {}
    }
}