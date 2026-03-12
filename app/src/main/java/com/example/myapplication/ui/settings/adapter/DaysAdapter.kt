package com.example.myapplication.ui.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class DaysAdapter(
    private val days: List<Int>,
    private val onSelected: (Int) -> Unit
) : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

    private var selectedPosition = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val radio: RadioButton = view.findViewById(R.id.radio)
        val text: TextView = view.findViewById(R.id.txtDays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_days, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = days.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val value = days[position]

        holder.text.text = "$value days"
        holder.radio.isChecked = position == selectedPosition

        holder.itemView.setOnClickListener {

            val old = selectedPosition
            selectedPosition = position

            notifyItemChanged(old)
            notifyItemChanged(position)

            onSelected(value)
        }
    }
}