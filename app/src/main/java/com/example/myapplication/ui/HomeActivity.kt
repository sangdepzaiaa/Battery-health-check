package com.example.myapplication.ui

import android.content.Intent
import android.content.IntentFilter
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.ui.BatteryUsage.BatteryUsageFragment
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.settings.SettingsFragment
import com.google.android.material.tabs.TabLayout

class HomeActivity : BaseActivity<ActivityHomeBinding>(
    inflater = ActivityHomeBinding::inflate
) {

    private val homeFragment = HomeFragment()
    private val batteryFragment = BatteryUsageFragment()
    private val settingsFragment = SettingsFragment()
    private var activeFragment: Fragment = homeFragment

    override fun initViewBase() {
        setupNav()
    }


    private fun setupNav() {
        // Khởi tạo fragments lần đầu (chỉ add 1 lần)
        if (supportFragmentManager.findFragmentByTag("home") == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragment_container, homeFragment, "home")
                add(R.id.fragment_container, batteryFragment, "battery").hide(batteryFragment)
                add(R.id.fragment_container, settingsFragment, "settings").hide(settingsFragment)
            }.commit()
        }

        activeFragment = homeFragment

        binding.bottomNavigation.apply {
            // Optional: nếu bạn muốn disable tint mặc định để selector icon hoạt động đúng (không bị override màu)
            // itemIconTintList = null  // Uncomment nếu icon bị tint sai màu

            // Hoặc dùng selector màu cho tint (nếu icon là monochrome)
            // itemIconTintList = ColorStateList.valueOf(...) hoặc từ resource

            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home     -> switchTo(homeFragment)
                    R.id.nav_battery  -> switchTo(batteryFragment)
                    R.id.nav_settings -> switchTo(settingsFragment)
                    else              -> false
                }
                true
            }

            // Set tab mặc định là Home
            selectedItemId = R.id.nav_home
        }
    }

    private fun switchTo(fragment: Fragment) {
        if (fragment == activeFragment) return

        supportFragmentManager.beginTransaction().apply {
            hide(activeFragment)
            show(fragment)
            commit()
            // commitAllowingStateLoss() nếu app hay crash do state loss (background/rotation)
        }

        activeFragment = fragment
    }
}