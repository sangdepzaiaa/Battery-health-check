package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

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

        val tech = intent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown"

        val health = when(intent?.getIntExtra(BatteryManager.EXTRA_HEALTH,0)){

            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            else -> "Unknown"
        }

        val plugged = when(intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED,0)){

            BatteryManager.BATTERY_PLUGGED_USB -> "USB"
            BatteryManager.BATTERY_PLUGGED_AC -> "AC"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
            else -> "Unplugged"
        }

        val status = when(intent?.getIntExtra(BatteryManager.EXTRA_STATUS,0)){

            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            else -> "Unknown"
        }

        return mapOf(

            "charge" to "$percent%",
            "voltage" to "${voltage/1000.0}v",
            "temperature" to "${temperature/10}°",
            "technology" to tech,
            "health" to health,
            "plugged" to plugged,
            "status" to status
        )
    }
}