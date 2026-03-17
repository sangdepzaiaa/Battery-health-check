package com.example.myapplication.utils

import android.content.Context
import android.os.BatteryManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.utils.BatteryHealthManager
import com.example.myapplication.utils.CalcBatteryBasic.calcHealth

class BatteryCheckWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    private lateinit var batteryManager: BatteryHealthManager

    override fun doWork(): Result {
        batteryManager = BatteryHealthManager(applicationContext)

        val design = batteryManager.getDesignCapacity()

        val health = calcHealth(applicationContext,design)

        NotificationHelper.showNotification(
            applicationContext,
            health
        )

        return Result.success()
    }
}