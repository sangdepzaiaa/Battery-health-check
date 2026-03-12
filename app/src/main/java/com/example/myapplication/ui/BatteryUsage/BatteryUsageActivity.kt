package com.example.myapplication.ui.BatteryUsage

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.databinding.ActivityBatteryUsageBinding
import com.example.myapplication.ui.HomeActivity
import com.example.myapplication.ui.settings.SettingsActivity
import com.example.myapplication.utils.tap

class BatteryUsageActivity : BaseActivity<ActivityBatteryUsageBinding>(
    inflater = ActivityBatteryUsageBinding::inflate
) {

    override fun initViewBase() {
        super.initViewBase()
//        setupBottomNavigation()
//        highlightCurrentTab()

        binding.run {
            btnCheck.setOnClickListener {

                val deviceName = "${Build.BRAND} ${Build.MODEL}"

                val capacity = getBatteryCapacity(this@BatteryUsageActivity)

                tvDevice.text = "Device: $deviceName"

                tvBattery.text = if (capacity > 0) {
                    "Battery capacity: ${capacity.toInt()} mAh"
                } else {
                    "Battery capacity: Not available"
                }
            }
        }
    }

    private fun getBatteryCapacity(context: Context): Double {
        return try {
            val powerProfileClass = Class.forName("com.android.internal.os.PowerProfile")

            val constructor = powerProfileClass.getConstructor(Context::class.java)

            val powerProfile = constructor.newInstance(context)

            val method = powerProfileClass.getMethod("getBatteryCapacity")

            method.invoke(powerProfile) as Double

        } catch (e: Exception) {
            e.printStackTrace()
            -1.0
        }
    }

//    private fun setupBottomNavigation() {
//        binding.layoutBottomNavHome.run {
//            clHome.tap {
//                navigateTo(HomeActivity::class.java)
//            }
//            clBatteryUsage.tap {
//                // đã ở đây rồi → có thể bỏ qua hoặc làm gì đó khác (ví dụ scroll to top)
//                // nhưng thường để trống hoặc chỉ highlight lại
//            }
//            clSettings.tap {
//                navigateTo(SettingsActivity::class.java)
//            }
//        }
//    }

//    private fun highlightCurrentTab() {
//        val currentClass = this::class.java
//
//        binding.layoutBottomNavHome.run {
//            // Reset tất cả về trạng thái không chọn
//            resetAllTabs()
//
//            when (currentClass) {
//                HomeActivity::class.java -> {
//                    icHome.setImageResource(R.drawable.ic_home_selected) // icon đã chọn
//                    tvHome.setTextColor(ContextCompat.getColor(this@BatteryUsageActivity, R.color.color_37C186))
//                    // nếu có background hoặc view khác → set ở đây
//                }
//                BatteryUsageActivity::class.java -> {
//                    icBatteryUsage.setImageResource(R.drawable.ic_battery_usage_selected)
//                    tvBatteryUsage.setTextColor(ContextCompat.getColor(this@BatteryUsageActivity, R.color.color_37C186))
//                }
//                SettingsActivity::class.java -> {
//                    icSettings.setImageResource(R.drawable.ic_settings_selected)
//                    tvSettings.setTextColor(ContextCompat.getColor(this@BatteryUsageActivity, R.color.color_37C186))
//                }
//            }
//        }
//    }
//
//    private fun resetAllTabs() {
//        binding.layoutBottomNavHome.run {
//            // Reset icon về trạng thái bình thường
//            icHome.setImageResource(R.drawable.ic_home_un_selected)
//            icBatteryUsage.setImageResource(R.drawable.ic_battery_usage_selected)
//            icSettings.setImageResource(R.drawable.ic_settings_un_selected)
//
//            // Reset text color về màu không chọn
//            val inactiveColor = ContextCompat.getColor(this@BatteryUsageActivity, R.color.color_CFD8DC)
//            tvHome.setTextColor(inactiveColor)
//            tvBatteryUsage.setTextColor(inactiveColor)
//            tvSettings.setTextColor(inactiveColor)
//        }
//    }

    private fun navigateTo(targetClass: Class<*>) {
        val currentClass = this::class.java
        if (currentClass == targetClass) return

        val intent = Intent(this, targetClass)
        startActivity(intent)
        overridePendingTransition(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
        finish()
    }
}