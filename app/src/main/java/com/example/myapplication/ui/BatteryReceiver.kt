package com.example.myapplication.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class BatteryReceiver(
    val onUpdate:(Int)->Unit
): BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {

        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,100)

        val percent = level * 100 / scale

        onUpdate(percent)
    }
}