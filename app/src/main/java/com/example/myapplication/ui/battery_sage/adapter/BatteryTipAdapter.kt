package com.example.myapplication.ui.battery_sage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.BatteryTipModel
import com.example.myapplication.databinding.LayoutItemBatteryTipBinding

class BatteryTipAdapter(
    private val list: List<BatteryTipModel>
) : RecyclerView.Adapter<BatteryTipAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutItemBatteryTipBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutItemBatteryTipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        holder.binding.apply {
            imgBatteryTip.setImageResource(item.icon)
            txvBatteryTip.text = item.title
            txvDes.text = item.description
        }
    }
}