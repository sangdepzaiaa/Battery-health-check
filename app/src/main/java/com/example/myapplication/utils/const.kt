package com.example.myapplication.utils

object const {

    const val PREFS_NAME = "battery_prefs"
    const val KEY_DESIGN_CAPACITY = "design_capacity"
    const val KEY_MEASURED_CAPACITY = "measured_capacity"
    const val KEY_SESSION_START_CHARGE = "session_start_charge"
    const val KEY_SESSION_START_TIME = "session_start_time"
    const val KEY_ACCUMULATED_MAH = "accumulated_mah"
    const val KEY_FULL_CHARGE_MAH = "full_charge_mah"
    const val KEY_CHARGE_CYCLES = "charge_cycles"

    val KEY_PATH_DEVICE =  listOf(
        // Generic / AOSP
        "/sys/class/power_supply/battery/charge_full",
        "/sys/class/power_supply/Battery/charge_full",

        // Qualcomm BMS
        "/sys/class/power_supply/bms/charge_full",
        "/sys/class/power_supply/BMS/charge_full",
        "/sys/class/power_supply/qcom-battery/charge_full",
        "/sys/class/power_supply/pm8941-bms/charge_full",
        "/sys/class/power_supply/pm8994-bms/charge_full",
        "/sys/class/power_supply/pm660-bms/charge_full",
        "/sys/class/power_supply/pm8150-bms/charge_full",
        "/sys/class/power_supply/pm8350-bms/charge_full",

        // Samsung
        "/sys/class/power_supply/sec-battery/charge_full",
        "/sys/class/power_supply/samsung-battery/charge_full",

        // Xiaomi / MIUI
        "/sys/class/power_supply/batt_id/charge_full",
        "/sys/class/power_supply/xiaomi-battery/charge_full",

        // MediaTek
        "/sys/class/power_supply/mt-battery/charge_full",
        "/sys/class/power_supply/mtk-battery/charge_full",
        "/sys/class/power_supply/mt6360_battery/charge_full",
        "/sys/class/power_supply/mt6358-gauge/charge_full",

        // Huawei / HiSilicon
        "/sys/class/power_supply/hisi-battery/charge_full",
        "/sys/class/power_supply/huawei-battery/charge_full",
        "/sys/class/power_supply/hi6521-battery/charge_full",

        // OnePlus
        "/sys/class/power_supply/oneplus-battery/charge_full",
        "/sys/class/power_supply/op-battery/charge_full",

        // Oppo / Realme / Vivo (BBK)
        "/sys/class/power_supply/oppo-battery/charge_full",
        "/sys/class/power_supply/oplus-battery/charge_full",
        "/sys/class/power_supply/vivo-battery/charge_full",
        "/sys/class/power_supply/realme-battery/charge_full",

        // Sony Xperia
        "/sys/class/power_supply/so34830_battery/charge_full",
        "/sys/class/power_supply/sony-battery/charge_full",

        // LG
        "/sys/class/power_supply/lg-battery/charge_full",
        "/sys/class/power_supply/lge-battery/charge_full",

        // Motorola
        "/sys/class/power_supply/moto-battery/charge_full",
        "/sys/class/power_supply/motorola-battery/charge_full",

        // ASUS
        "/sys/class/power_supply/asus-battery/charge_full",
        "/sys/class/power_supply/ASUSbattery/charge_full",

        // Nothing Phone
        "/sys/class/power_supply/nothing-battery/charge_full",

        // Fairphone
        "/sys/class/power_supply/fairphone-battery/charge_full",

        // Generic fallback names
        "/sys/class/power_supply/main-battery/charge_full",
        "/sys/class/power_supply/main_battery/charge_full",
        "/sys/class/power_supply/bat/charge_full",
        "/sys/class/power_supply/BAT/charge_full",
        "/sys/class/power_supply/BAT0/charge_full",
        "/sys/class/power_supply/BAT1/charge_full",
        "/sys/class/power_supply/charger/charge_full",
        "/sys/class/power_supply/fg/charge_full",           // fuel gauge
        "/sys/class/power_supply/fuel_gauge/charge_full",
        "/sys/class/power_supply/fuelgauge/charge_full",
        "/sys/class/power_supply/ds2784-fuelgauge/charge_full",
        "/sys/class/power_supply/ds2746-battery/charge_full",
        "/sys/class/power_supply/max17040-battery/charge_full",
        "/sys/class/power_supply/max17042-battery/charge_full",
        "/sys/class/power_supply/max17048-battery/charge_full",
        "/sys/class/power_supply/max1704x-battery/charge_full",
        "/sys/class/power_supply/max77818-battery/charge_full"
    )

}