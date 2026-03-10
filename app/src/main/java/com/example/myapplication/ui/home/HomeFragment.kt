package com.example.myapplication.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.BatteryAdapter
import com.example.myapplication.ui.BatteryInfoUtil
import com.example.myapplication.ui.BatteryItem
import com.example.myapplication.ui.BatteryReceiver
import com.example.myapplication.ui.TYPE_BIG
import com.example.myapplication.ui.TYPE_SMALL
import com.google.android.material.tabs.TabLayout

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    inflater = FragmentHomeBinding::inflate
) {

    private var adapter: BatteryAdapter? = null
    private var batteryReceiver: BroadcastReceiver? = null

    override fun initView() {
        super.initView()
        setUpTablayout()
        setUpInfoBattery()
        listenBatteryChange()
    }

    private fun setUpTablayout() {
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

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setUpInfoBattery() {

        val info = BatteryInfoUtil.getBatteryInfo(requireContext())

        val list = mutableListOf(
            BatteryItem("Voltage", info["Voltage"] ?: "unKnown", R.drawable.ic_voltage, TYPE_SMALL),
            BatteryItem("Health", info["health"] ?: "unKnown", R.drawable.ic_health, TYPE_SMALL),
            BatteryItem("Charge", info["charge"] ?: "unKnown", R.drawable.ic_battery_change, TYPE_BIG),
            BatteryItem("Temperature", info["temperature"] ?: "unKnown", R.drawable.ic_temperature, TYPE_SMALL),
            BatteryItem("Plugged", info["plugged"] ?: "unKnown", R.drawable.ic_plugged, TYPE_SMALL),
            BatteryItem("Technology", info["technology"] ?: "unKnown", R.drawable.ic_technology, TYPE_SMALL),
            BatteryItem("Status", info["status"] ?: "unKnown", R.drawable.ic_status, TYPE_SMALL),

        )

        binding.layoutTabInfo.rvBattery.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        adapter = BatteryAdapter(list)

        binding.layoutTabInfo.rvBattery.adapter = adapter
    }

    private fun listenBatteryChange() {

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)

        batteryReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) ?: 0
                val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100

                val percent = level * 100 / scale

                val voltage = intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0) ?: 0
                val temperature = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0) ?: 0

                val tech = intent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown"

                val health = when(intent?.getIntExtra(BatteryManager.EXTRA_HEALTH,0)){
                    BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
                    BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
                    BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
                    else -> "Unknown"
                }

                val plugged = when(intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED,0)){
                    BatteryManager.BATTERY_PLUGGED_USB -> "USB"
                    BatteryManager.BATTERY_PLUGGED_AC -> "AC"
                    BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
                    else -> "Unplugged"
                }

                val status = when(intent?.getIntExtra(BatteryManager.EXTRA_STATUS,0)){
                    BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
                    BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
                    BatteryManager.BATTERY_STATUS_FULL -> "Full"
                    else -> "Unknown"
                }

                val newList = listOf(
                    BatteryItem("Voltage", "${voltage/1000.0}v", R.drawable.ic_voltage, TYPE_SMALL),
                    BatteryItem("Health", health, R.drawable.ic_health, TYPE_SMALL),
                    BatteryItem("Charge", "$percent%", R.drawable.ic_battery_change, TYPE_BIG),
                    BatteryItem("Temperature", "${temperature/10}°", R.drawable.ic_temperature, TYPE_SMALL),
                    BatteryItem("Plugged", plugged, R.drawable.ic_plugged, TYPE_SMALL),
                    BatteryItem("Technology", tech, R.drawable.ic_technology, TYPE_SMALL),
                    BatteryItem("Status", status, R.drawable.ic_status, TYPE_SMALL)
                )

                adapter?.updateData(newList)
            }
        }

        requireContext().registerReceiver(batteryReceiver, filter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        batteryReceiver?.let {
            requireContext().unregisterReceiver(it)
        }
    }
}