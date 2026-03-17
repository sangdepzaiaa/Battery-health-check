package com.example.myapplication.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log

object CalcBatteryBasic {
    /**
     * Tính dung lượng thực đo được (measured capacity) từ chargeCounter khi đầy.
     */

    // tính dung lượng pin ở thời điểm pin đầy ở bất kỳ lúc nào sạc đầy, sau đó lưu vào sharePre,
    // đo nhiều lần rồi lấy trung bình

    fun getMeasuredCapacityWhenFull(context: Context): Int {

        val intent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        ) ?: return 0

        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
       // status != BatteryManager.BATTERY_STATUS_FULL
        val percent = if (level >= 0 && scale > 0) {
            level * 100 / scale
        } else {
            -1
        }

        // chỉ đo khi pin đầy
        if (percent != 100 || status != BatteryManager.BATTERY_STATUS_FULL) {
            return 0
        }

        val batteryManager =
            context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        val chargeCounter =
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)

        if (chargeCounter <= 0) return 0

        val measuredCapacity = chargeCounter / 1000

        Log.d("Battery", "chargeCounter = $chargeCounter µAh")
        Log.d("Battery", "measured capacity = $measuredCapacity mAh")

        return measuredCapacity
    }

    fun saveMeasuredCapacity(context: Context, capacity: Int) {

        if (capacity !in 1000..8000) return

        val prefs = context.getSharedPreferences("battery_data", Context.MODE_PRIVATE)

        val old = prefs.getString("capacity_list", "") ?: ""

        val list =
            if (old.isEmpty()) mutableListOf()
            else old.split(",").toMutableList()

        val last = list.lastOrNull()?.toIntOrNull()

        // chống spam + trùng
        if (last != null && kotlin.math.abs(last - capacity) < 50) {
            return
        }

        list.add(capacity.toString())

        val numbers = list.mapNotNull { it.toIntOrNull() }

        if (numbers.isEmpty()) return

        val avg = numbers.sum() / numbers.size

        val filtered = numbers.filter {
            kotlin.math.abs(it - avg) < avg * 0.15
        }

        val last30 = filtered.takeLast(30)

        prefs.edit()
            .putString("capacity_list", last30.joinToString(","))
            .apply()
    }

    fun getAverageCapacity(context: Context): Int {

        val prefs = context.getSharedPreferences("battery_data", Context.MODE_PRIVATE)

        val data = prefs.getString("capacity_list", "") ?: ""

        if (data.isEmpty()) return 0

        val numbers = data.split(",").mapNotNull { it.toIntOrNull() }

        if (numbers.isEmpty()) return 0

        return numbers.sum() / numbers.size
    }

    fun calcHealth(context: Context, designCapacity: Int): Int {

        val measured = getMeasuredCapacityWhenFull(context)

        if (measured > 0) {
            saveMeasuredCapacity(context, measured)
        }

        val avgCapacity = getAverageCapacity(context)

        if (designCapacity <= 0 || avgCapacity <= 0) return 0

        val health = (avgCapacity * 100) / designCapacity
        Log.d("he", "health = $health")

        return health.coerceAtMost(100)
    }

    //Dung lượng tối đa ước tính của pin tại thời điểm hiện tại
    fun getMeasuredCapacityTime(context: Context): Int {

        val batteryManager =
            context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        val chargeCounter =
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)

        val intent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) ?: 0

        if (chargeCounter <= 0 || level <= 0) return 0

        val capacity = (chargeCounter / (level / 100f)) / 1000

        return capacity.toInt()
    }




    //Dung lượng  của pin tại thời điểm hiện tại
    fun getMeasuredCapacity(context: Context): Int {

        val batteryManager =
            context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        val chargeCounter =
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)

        if (chargeCounter <= 0) return 0

        val mah = chargeCounter / 1000

        Log.d("Battery", "chargeCounter = $chargeCounter µAh")
        Log.d("Battery", "measuredCapacity = $mah mAh")

        return mah
    }



}