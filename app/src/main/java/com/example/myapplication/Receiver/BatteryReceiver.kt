package com.example.myapplication.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myapplication.utils.CalcBatteryAdvanced

class BatteryReceiver2(
    private val engine: CalcBatteryAdvanced
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
            engine.update(intent)
        }
    }
}