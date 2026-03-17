package com.example.myapplication.data

data class BatteryItem(
    val title: String,
    var value: String,
    val icon: Int,
    val type: Int
)

const val TYPE_SMALL = 0
const val TYPE_BIG = 1


