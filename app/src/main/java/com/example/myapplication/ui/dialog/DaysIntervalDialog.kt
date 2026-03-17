package com.example.myapplication.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.base.BaseDialog
import com.example.myapplication.databinding.LayoutDialogSetIntervalBinding
import com.example.myapplication.ui.settings.adapter.DaysAdapter

class DaysIntervalDialog(
    context: Context,
    private val onDaysSelected: (Int) -> Unit
) : BaseDialog<LayoutDialogSetIntervalBinding>(context) {

    private var selectedDays = 3

    override fun getViewBinding(): LayoutDialogSetIntervalBinding {
        return LayoutDialogSetIntervalBinding.inflate(LayoutInflater.from(context))
    }

    override fun initViews() {

        val daysList = listOf(3,4,5,6,7)

        val adapter = DaysAdapter(daysList) { days ->
            selectedDays = days
        }

        dBinding.recyclerDays.layoutManager = LinearLayoutManager(context)
        dBinding.recyclerDays.adapter = adapter

        dBinding.btnSetInterval.setOnClickListener {

            onDaysSelected(selectedDays)

            dismiss()
        }
    }
}