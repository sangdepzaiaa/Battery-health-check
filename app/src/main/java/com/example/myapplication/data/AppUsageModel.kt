package com.example.myapplication.data

import android.graphics.drawable.Drawable

data class AppUsageModel(
    val appName: String,
    val packageName: String,
    val icon: Drawable,
    val totalTime: Long,
    val lastUsed: Long,
    val isSystemApp: Boolean,
    val batteryPercent: Int
)
