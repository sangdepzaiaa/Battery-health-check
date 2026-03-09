package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class BatteryAdapter(
    private val list: List<BatteryItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int) = list[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_BIG) {

            val v = inflater.inflate(R.layout.layout_item_charge_card, parent, false)
            BigVH(v)

        } else {

            val v = inflater.inflate(R.layout.layout_item_battery_card, parent, false)
            SmallVH(v)
        }
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = list[position]

        if(holder is SmallVH){

            holder.icon.setImageResource(item.icon)
            holder.title.text = item.title
            holder.value.text = item.value

        }

        if(holder is BigVH){

            holder.icon.setImageResource(item.icon)
            holder.title.text = item.title
            holder.value.text = item.value

            val percent = item.value.replace("%","").toInt()

            holder.wave.setProgress(percent)

            holder.wave.startAnimation()

        }
    }

    class SmallVH(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.icon)
        val title: TextView = v.findViewById(R.id.title)
        val value: TextView = v.findViewById(R.id.value)
    }

    class BigVH(v: View) : RecyclerView.ViewHolder(v) {
        val icon: ImageView = v.findViewById(R.id.icon)
        val title: TextView = v.findViewById(R.id.title)
        val value: TextView = v.findViewById(R.id.value)
        val wave: BatteryWaveView = v.findViewById(R.id.wave)
    }

    fun updateCharge(percent:Int){

        list.find { it.title=="Charge" }?.value = "$percent%"

        notifyDataSetChanged()
    }
}