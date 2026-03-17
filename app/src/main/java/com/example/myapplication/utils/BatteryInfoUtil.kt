package com.example.myapplication.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.example.myapplication.R

object BatteryInfoUtil {

    fun getBatteryInfo(context: Context): Map<String,String>{

        val intent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL,-1) ?: 0
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE,-1) ?: 100

        val percent = level * 100 / scale

        val voltage = intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0) ?: 0

        val temperature = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0) ?: 0

        val tech = intent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: context.getString(R.string.unKnown)

        val health = when(intent?.getIntExtra(BatteryManager.EXTRA_HEALTH,0)){

            BatteryManager.BATTERY_HEALTH_GOOD -> context.getString(R.string.good)
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> context.getString(R.string.overheat)
            BatteryManager.BATTERY_HEALTH_DEAD -> context.getString(R.string.dead)
            else -> context.getString(R.string.unKnown)
        }

        val plugged = when(intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED,0)){

            BatteryManager.BATTERY_PLUGGED_USB -> context.getString(R.string.usb)
            BatteryManager.BATTERY_PLUGGED_AC -> context.getString(R.string.ac)
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> context.getString(R.string.wireless)
            else -> context.getString(R.string.unplugged)
        }

        val status = when(intent?.getIntExtra(BatteryManager.EXTRA_STATUS,0)){

            BatteryManager.BATTERY_STATUS_CHARGING -> context.getString(R.string.charging)
            BatteryManager.BATTERY_STATUS_DISCHARGING -> context.getString(R.string.discharging)
            BatteryManager.BATTERY_STATUS_FULL -> context.getString(R.string.full)
            else -> context.getString(R.string.unKnown)
        }

        return mapOf(

            context.getString(R.string.charging) to "$percent%",
            context.getString(R.string.voltage) to "${voltage/1000.0}v",
            context.getString(R.string.temperature) to "${temperature/10}°",
            context.getString(R.string.technology) to tech,
            context.getString(R.string.health) to health,
            context.getString(R.string.plugged) to plugged,
            context.getString(R.string.status) to status
        )
    }
}