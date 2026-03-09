package com.example.myapplication.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.databinding.ActivitySettingsBinding
import com.example.myapplication.ui.BatteryUsage.BatteryUsageActivity
import com.example.myapplication.ui.HomeActivity
import com.example.myapplication.utils.tap

class SettingsActivity : BaseActivity<ActivitySettingsBinding>(
    inflater = ActivitySettingsBinding::inflate
) {

    override fun initViewBase() {
        super.initViewBase()
        setupBottomNavigation()
        highlightCurrentTab()
    }

    private fun setupBottomNavigation() {
        binding.layoutBottomNavHome.run {
            clHome.tap {
                navigateTo(HomeActivity::class.java)
            }
            clBatteryUsage.tap {
                // đã ở đây rồi → có thể bỏ qua hoặc làm gì đó khác (ví dụ scroll to top)
                // nhưng thường để trống hoặc chỉ highlight lại
            }
            clSettings.tap {
                navigateTo(SettingsActivity::class.java)
            }
        }
    }

    private fun highlightCurrentTab() {
        val currentClass = this::class.java

        binding.layoutBottomNavHome.run {
            // Reset tất cả về trạng thái không chọn
            resetAllTabs()

            when (currentClass) {
                HomeActivity::class.java -> {
                    icHome.setImageResource(R.drawable.ic_home_selected) // icon đã chọn
                    tvHome.setTextColor(ContextCompat.getColor(this@SettingsActivity, R.color.color_37C186))
                    // nếu có background hoặc view khác → set ở đây
                }
                BatteryUsageActivity::class.java -> {
                    icBatteryUsage.setImageResource(R.drawable.ic_battery_usage_selected)
                    tvBatteryUsage.setTextColor(ContextCompat.getColor(this@SettingsActivity, R.color.color_37C186))
                }
                SettingsActivity::class.java -> {
                    icSettings.setImageResource(R.drawable.ic_settings_selected)
                    tvSettings.setTextColor(ContextCompat.getColor(this@SettingsActivity, R.color.color_37C186))
                }
            }
        }
    }

    private fun resetAllTabs() {
        binding.layoutBottomNavHome.run {
            // Reset icon về trạng thái bình thường
            icHome.setImageResource(R.drawable.ic_home_un_selected)
            icBatteryUsage.setImageResource(R.drawable.ic_battery_un_selected)
            icSettings.setImageResource(R.drawable.ic_settings_selected)

            // Reset text color về màu không chọn
            val inactiveColor = ContextCompat.getColor(this@SettingsActivity, R.color.color_CFD8DC)
            tvHome.setTextColor(inactiveColor)
            tvBatteryUsage.setTextColor(inactiveColor)
            tvSettings.setTextColor(inactiveColor)
        }
    }

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