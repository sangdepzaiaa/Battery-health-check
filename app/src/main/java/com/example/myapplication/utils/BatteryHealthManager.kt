package com.example.myapplication.utils

import android.content.Context

class BatteryHealthManager(private val context: Context) {

    private val prefs =
        context.getSharedPreferences("battery_health", Context.MODE_PRIVATE)

    fun getDesignCapacity(): Int {

        val saved = prefs.getInt("design_capacity", 0)
        if (saved > 0) return saved

        return try {
            val clazz = Class.forName("com.android.internal.os.PowerProfile")
            val constructor = clazz.getConstructor(Context::class.java)
            val instance = constructor.newInstance(context)
            val method = clazz.getMethod("getBatteryCapacity")
            (method.invoke(instance) as Double).toInt()
        } catch (e: Exception) {
            0
        }
    }

    fun saveDesignCapacity(value: Int) {
        prefs.edit().putInt("design_capacity", value).apply()
    }

    fun getEstimatedCapacity(): Int {
        return prefs.getInt("estimated_capacity", 0)
    }

    fun saveEstimatedCapacity(value: Int) {
        prefs.edit().putInt("estimated_capacity", value).apply()
    }

    fun setScanDone() {
        prefs.edit().putBoolean("scan_done", true).apply()
    }

    fun isScanDone(): Boolean {
        return prefs.getBoolean("scan_done", false)
    }
}