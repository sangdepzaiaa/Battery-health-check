package com.example.myapplication.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dino.ads.admob.AdmobUtils
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.ui.BatteryUsage.BatteryUsageActivity
import com.example.myapplication.ui.settings.SettingsActivity
import com.example.myapplication.utils.navigateTo
import com.example.myapplication.utils.tap
import com.google.android.material.tabs.TabLayout
import kotlin.jvm.java

class HomeActivity : BaseActivity<ActivityHomeBinding>(
    inflater = ActivityHomeBinding::inflate
) {
    private var adapter: BatteryAdapter? =null
    override fun initViewBase() {
        setUpTablayout()
        setUpInfoBattery()
        setUpNavigation()
    }

    private fun setUpTablayout(){
        binding.layoutTabLayout.tabLayout.addTab(
            binding.layoutTabLayout.tabLayout.newTab().setText("Information")
        )
        binding.layoutTabLayout.tabLayout.addTab(
            binding.layoutTabLayout.tabLayout.newTab().setText("Health")
        )

        binding.layoutTabLayout.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    binding.layoutTabInfo.rvBattery.visibility = View.VISIBLE
                    binding.layoutTabHealth.layoutTabHealth.visibility = View.GONE
                } else {
                    binding.layoutTabInfo.rvBattery.visibility = View.GONE
                    binding.layoutTabHealth.layoutTabHealth.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun setUpInfoBattery(){
        val info = BatteryInfoUtil.getBatteryInfo(this)

        val list = listOf(

            BatteryItem("Voltage",info["voltage"]!!,R.drawable.ic_voltage,TYPE_SMALL),

            BatteryItem("Health",info["health"]!!,R.drawable.ic_health,TYPE_SMALL),

            BatteryItem("Charge",info["charge"]!!,R.drawable.ic_battery_change,TYPE_BIG),

            BatteryItem("Temperature",info["temperature"]!!,R.drawable.ic_temperature,TYPE_SMALL),

            BatteryItem("Plugged",info["plugged"]!!,R.drawable.ic_plugged,TYPE_SMALL),

            BatteryItem("Technology",info["technology"]!!,R.drawable.ic_technology,TYPE_SMALL),

            BatteryItem("Status",info["status"]!!,R.drawable.ic_status,TYPE_SMALL)
        )

        binding.layoutTabInfo.rvBattery.layoutManager =
            StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        binding.layoutTabInfo.rvBattery.adapter = BatteryAdapter(list)


        val receiver = BatteryReceiver {

            adapter?.updateCharge(it)

        }

        registerReceiver(
            receiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
    }

    private fun setUpNavigation() {

        binding.layoutBottomNavHome.run {
            clHome.tap {
                navigateTo(HomeActivity::class.java)
            }

            clBatteryUsage.tap {
                navigateTo(BatteryUsageActivity::class.java)
            }

            clSettings.tap {
                navigateTo(SettingsActivity::class.java)
            }
        }
    }




}