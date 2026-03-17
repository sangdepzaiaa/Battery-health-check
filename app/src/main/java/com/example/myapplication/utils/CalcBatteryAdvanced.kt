package com.example.myapplication.utils

import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class CalcBatteryAdvanced(private val context: Context) {

    private val batteryManager =
        context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    private val prefs =
        context.getSharedPreferences("battery_engine", Context.MODE_PRIVATE)

    private var startPercent = -1
    private var lastTime = 0L
    private var chargedMah = 0.0
    private var charging = false

    fun update(intent: Intent) {

        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val percent = level * 100 / scale

        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10

        val isCharging =
            status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL

        if (isCharging && !charging) startCharge(percent)

        if (isCharging) measureCurrent()

        if (!isCharging && charging) finishCharge(percent)

        if (percent == 100 && status == BatteryManager.BATTERY_STATUS_FULL) {
            measureChargeCounter()
        }

        charging = isCharging
    }

    private fun startCharge(percent: Int) {
        startPercent = percent
        chargedMah = 0.0
        lastTime = System.currentTimeMillis()
    }

    private fun measureCurrent() {

        val currentNow =
            batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CURRENT_NOW
            )

        val currentmA = kotlin.math.abs(currentNow / 1000.0)

        if (currentmA < 200) return

        val now = System.currentTimeMillis()

        val hours = (now - lastTime) / 3600000.0

        chargedMah += currentmA * hours

        lastTime = now
    }

    private fun finishCharge(endPercent: Int) {

        val delta = endPercent - startPercent

        if (delta < 15) return

        val capacity = chargedMah * 100 / delta

        if (capacity < 1000 || capacity > 8000) return

        saveData(capacity.toInt())
    }

    private fun measureChargeCounter() {

        val counter =
            batteryManager.getIntProperty(
                BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER
            )

        if (counter <= 0) return

        val mah = counter / 1000

        if (mah in 1000..8000) {
            saveData(mah)
        }
    }

    private fun saveData(value: Int) {

        val old = prefs.getString("data", "") ?: ""

        val list =
            if (old.isEmpty()) mutableListOf()
            else old.split(",").toMutableList()

        list.add(value.toString())

        val numbers = list.mapNotNull { it.toIntOrNull() }

        val avg = if (numbers.isNotEmpty())
            numbers.sum() / numbers.size
        else value

        val filtered =
            numbers.filter {
                kotlin.math.abs(it - avg) < avg * 0.15
            }

        val last30 = filtered.takeLast(30)

        prefs.edit()
            .putString("data", last30.joinToString(","))
            .apply()
    }

    fun getCapacity(): Int {

        val data = prefs.getString("data", "") ?: ""

        if (data.isEmpty()) return 0

        val nums = data.split(",").mapNotNull { it.toIntOrNull() }

        if (nums.isEmpty()) return 0

        return nums.sum() / nums.size
    }

    fun getHealth(designCapacity: Int): Int {

        val cap = getCapacity()

        if (cap == 0 || designCapacity <= 0) return 0

        val health = cap * 100 / designCapacity

        return health.coerceAtMost(100)
    }
}