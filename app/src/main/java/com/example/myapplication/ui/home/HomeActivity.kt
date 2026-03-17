package com.example.myapplication.ui.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dino.rate.addActivity
import com.example.myapplication.R
import com.example.myapplication.Receiver.BatteryReceiver2
import com.example.myapplication.RemoteConfig
import com.example.myapplication.ads.AdsManager
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.ui.battery_sage.BatteryUsageFragment
import com.example.myapplication.ui.settings.SettingsFragment
import com.example.myapplication.utils.CalcBatteryAdvanced

class HomeActivity : BaseActivity<ActivityHomeBinding>(
    inflater = ActivityHomeBinding::inflate
) {

    private val homeFragment = HomeFragment()
    private val batteryFragment = BatteryUsageFragment()
    private val settingsFragment = SettingsFragment()
    private var activeFragment: Fragment = homeFragment

    lateinit var engine: CalcBatteryAdvanced
    lateinit var receiver: BatteryReceiver2


    override fun initViewBase() {
        setupNav()
        createNotificationChannel()
        requestNotificationPermission()
        engine = CalcBatteryAdvanced(this)
        receiver = BatteryReceiver2(engine)

        registerReceiver(
            receiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        AdsManager.loadAndShowNative(this, RemoteConfig.NATIVE_INTRO_FULL, binding.frEnd)

    }

    override fun onStart() {
        super.onStart()
        AdsManager.loadAndShowBanner(this, RemoteConfig.BANNER_HOME,binding.frEnd)

    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "battery_channel",
                "Battery Health",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(
                NotificationManager::class.java
            )

            manager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
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