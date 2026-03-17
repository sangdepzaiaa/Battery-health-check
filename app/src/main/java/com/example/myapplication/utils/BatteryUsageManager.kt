package com.example.myapplication.utils

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.core.R
import com.example.myapplication.data.AppUsageModel

object BatteryUsageManager {

    fun getInstalledApps(context: Context): List<ApplicationInfo> {
        val pm = context.packageManager
        return pm.getInstalledApplications(PackageManager.GET_META_DATA)
    }

    fun getUsageStats(context: Context): Map<String, UsageStats> {

        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val end = System.currentTimeMillis()
        val start = end - 1000 * 60 * 60 * 24

        val stats = usm.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            start,
            end
        )

        val map = mutableMapOf<String, UsageStats>()

        stats.forEach {
            map[it.packageName] = it
        }

        return map
    }

    fun loadAppUsage(context: Context): List<AppUsageModel> {

        val pm = context.packageManager
        val apps = getInstalledApps(context)
        val usageMap = getUsageStats(context)

        val result = mutableListOf<AppUsageModel>()

        val totalUsage =
            usageMap.values.sumOf { it.totalTimeInForeground }

        for (app in apps) {

            // bỏ app không launch được
            //if (pm.getLaunchIntentForPackage(app.packageName) == null) continue

            val usage = usageMap[app.packageName]

            val totalTime = usage?.totalTimeInForeground ?: 0L
            val lastUsed = usage?.lastTimeUsed ?: 0L

            val isSystem =
                (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0

            val name = pm.getApplicationLabel(app).toString()
            val icon = pm.getApplicationIcon(app)

            val percent = if (isSystem) {

                0

            } else if (totalUsage > 0 && totalTime > 0) {

                val base =
                    ((totalTime.toDouble() / totalUsage) * 100).toInt()

                (base + (0..3).random()).coerceAtMost(99)

            } else {
                0
            }

            result.add(
                AppUsageModel(
                    appName = name,
                    packageName = app.packageName,
                    icon = icon,
                    totalTime = totalTime,
                    lastUsed = lastUsed,
                    isSystemApp = isSystem,
                    batteryPercent = percent
                )
            )
        }

        return result.sortedByDescending { it.totalTime }
    }

    fun formatTotalTime(time: Long,context: Context): String {

        val minutes = time / 1000 / 60

        return if (minutes < 1) {
            context.getString(com.example.myapplication.R.string.less_than_1_min)
        } else {
            "$minutes ${context.getString(com.example.myapplication.R.string.less_than_1_min)}"
        }
    }

    fun formatLastUsed(lastUsed: Long,context: Context): String {

        if (lastUsed == 0L) return context.getString(com.example.myapplication.R.string.some_time_ago)

        val diff = System.currentTimeMillis() - lastUsed
        val minutes = diff / 1000 / 60

        return when {
            minutes < 1 -> context.getString(com.example.myapplication.R.string.just_now)
            minutes < 60 -> "$minutes ${context.getString(com.example.myapplication.R.string.min_ago)}"
            minutes < 1440 -> "${minutes / 60} ${context.getString(com.example.myapplication.R.string.h_ago)}"
            else -> context.getString(com.example.myapplication.R.string.some_time_ago)
        }
    }
}