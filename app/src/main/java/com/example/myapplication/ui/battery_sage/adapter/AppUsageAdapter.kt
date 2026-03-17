package com.example.myapplication.ui.battery_sage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.AppUsageModel
import com.example.myapplication.databinding.LayoutItemBatteryUsageBinding
import com.example.myapplication.utils.BatteryUsageManager
import kotlin.coroutines.coroutineContext

class AppUsageAdapter :
    ListAdapter<AppUsageModel, AppUsageAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = LayoutItemBatteryUsageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: LayoutItemBatteryUsageBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(item: AppUsageModel) {
            val context = itemView.context

            binding.imgApp.setImageDrawable(item.icon)

            binding.txvName.text = item.appName

            binding.txvUseTime.text =
                "${context.getString(R.string.total)} ${BatteryUsageManager.formatTotalTime(item.totalTime,context)}"

            binding.txvTime.text =
                BatteryUsageManager.formatLastUsed(item.lastUsed,context)

            binding.txvBatteryUsage.text =
                "${item.batteryPercent}%"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AppUsageModel>() {

        override fun areItemsTheSame(
            oldItem: AppUsageModel,
            newItem: AppUsageModel
        ): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(
            oldItem: AppUsageModel,
            newItem: AppUsageModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}