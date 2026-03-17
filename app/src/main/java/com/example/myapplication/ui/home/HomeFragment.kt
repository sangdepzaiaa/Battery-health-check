package com.example.myapplication.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dino.ads.admob.AdmobUtils
import com.dino.rate.addActivity
import com.dino.rate.invisible
import com.example.myapplication.R
import com.example.myapplication.RemoteConfig
import com.example.myapplication.ads.AdsManager
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.databinding.LayoutDialogSetBatteryBinding
import com.example.myapplication.ui.home.adapter.BatteryAdapter
import com.example.myapplication.utils.BatteryInfoUtil
import com.example.myapplication.data.BatteryItem
import com.example.myapplication.data.TYPE_BIG
import com.example.myapplication.data.TYPE_SMALL
import com.example.myapplication.ui.battery_sage.BatteryTipsActivity
import com.example.myapplication.utils.BatteryHealthManager
import com.example.myapplication.utils.CalcBatteryAdvanced
import com.example.myapplication.utils.CalcBatteryBasic
import com.example.myapplication.utils.CalcBatteryBasic.calcHealth
import com.example.myapplication.utils.tap
import com.google.android.material.tabs.TabLayout

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    inflater = FragmentHomeBinding::inflate
) {

    private var adapter: BatteryAdapter? = null
    private var batteryReceiver: BroadcastReceiver? = null

    private lateinit var batteryManager: BatteryHealthManager




    override fun initView() {
        super.initView()
        setUpTablayout()
        setUpInfoBattery()
        listenBatteryChange()

        batteryManager = BatteryHealthManager(requireContext())

        setupClick()
        loadBatteryInfo()

        binding.layoutToolBar.imgInfo.tap {
            startActivity(Intent(requireContext(), BatteryTipsActivity::class.java))
        }

    }



    private fun setUpTablayout() {
        binding.layoutTabLayout.tabLayout.addTab(
            binding.layoutTabLayout.tabLayout.newTab().setText(context?.getString(R.string.information))
        )
        binding.layoutTabLayout.tabLayout.addTab(
            binding.layoutTabLayout.tabLayout.newTab().setText(context?.getString(R.string.health))
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
            BatteryItem("${context?.getString(R.string.voltage)}", info["${context?.getString(R.string.voltage)}"] ?: "${context?.getString(R.string.unKnown)}", R.drawable.ic_voltage, TYPE_SMALL),
            BatteryItem("${context?.getString(R.string.health)}", info["${context?.getString(R.string.health)}"] ?: "${context?.getString(R.string.unKnown)}", R.drawable.ic_health, TYPE_SMALL),
            BatteryItem("${context?.getString(R.string.charge)}", info["${context?.getString(R.string.charge)}"] ?: "${context?.getString(R.string.unKnown)}", R.drawable.ic_battery_change, TYPE_BIG),
            BatteryItem("${context?.getString(R.string.temperature)}", info["${context?.getString(R.string.temperature)}"] ?: "${context?.getString(R.string.unKnown)}", R.drawable.ic_temperature, TYPE_SMALL),
            BatteryItem("${context?.getString(R.string.plugged)}", info["${context?.getString(R.string.plugged)}"] ?: "${context?.getString(R.string.unKnown)}", R.drawable.ic_plugged, TYPE_SMALL),
            BatteryItem("${context?.getString(R.string.technology)}", info["${context?.getString(R.string.technology)}"] ?: "${context?.getString(R.string.unKnown)}", R.drawable.ic_technology, TYPE_SMALL),
            BatteryItem("${context?.getString(R.string.status)}", info["${context?.getString(R.string.status)}"] ?: "${context?.getString(R.string.unKnown)}", R.drawable.ic_status, TYPE_SMALL),

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

                val tech = intent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "${context?.getString(R.string.unKnown)}"

                val health = when(intent?.getIntExtra(BatteryManager.EXTRA_HEALTH,0)){
                    BatteryManager.BATTERY_HEALTH_GOOD -> "${context?.getString(R.string.good)}"
                    BatteryManager.BATTERY_HEALTH_OVERHEAT -> "${context?.getString(R.string.overheat)}"
                    BatteryManager.BATTERY_HEALTH_DEAD -> "${context?.getString(R.string.dead)}"
                    BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "${context?.getString(R.string.over_Voltage)}"
                    BatteryManager.BATTERY_HEALTH_COLD -> "${context?.getString(R.string.cold)}"
                    else -> "${context?.getString(R.string.unKnown)}"
                }

                val plugged = when(intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED,0)){
                    BatteryManager.BATTERY_PLUGGED_USB -> "${context?.getString(R.string.usb)}"
                    BatteryManager.BATTERY_PLUGGED_AC -> "${context?.getString(R.string.ac)}"
                    BatteryManager.BATTERY_PLUGGED_WIRELESS -> "${context?.getString(R.string.wireless)}"
                    else -> "${context?.getString(R.string.unplugged)}"
                }

                val status = when(intent?.getIntExtra(BatteryManager.EXTRA_STATUS,0)){
                    BatteryManager.BATTERY_STATUS_CHARGING -> "${context?.getString(R.string.charging)}"
                    BatteryManager.BATTERY_STATUS_DISCHARGING -> "${context?.getString(R.string.discharging)}"
                    BatteryManager.BATTERY_STATUS_FULL -> "${context?.getString(R.string.full)}"
                    else -> "${context?.getString(R.string.unKnown)}"
                }

                val newList = listOf(
                    BatteryItem("${context?.getString(R.string.voltage)}", "${voltage/1000.0}v", R.drawable.ic_voltage, TYPE_SMALL),
                    BatteryItem("${context?.getString(R.string.health)}", health, R.drawable.ic_health, TYPE_SMALL),
                    BatteryItem("${context?.getString(R.string.charge)}", "$percent%", R.drawable.ic_battery_change, TYPE_BIG),
                    BatteryItem("${context?.getString(R.string.temperature)}", "${temperature/10}°", R.drawable.ic_temperature, TYPE_SMALL),
                    BatteryItem("${context?.getString(R.string.plugged)}", plugged, R.drawable.ic_plugged, TYPE_SMALL),
                    BatteryItem("${context?.getString(R.string.technology)}", tech, R.drawable.ic_technology, TYPE_SMALL),
                    BatteryItem("${context?.getString(R.string.status)}", status, R.drawable.ic_status, TYPE_SMALL)
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

    private fun setupClick() {

        binding.layoutTabHealth.txvSetDesign.setOnClickListener {
            showInputCapacityDialog()
        }

        binding.layoutTabHealth.btnOk.setOnClickListener {
            startBatteryScan()
        }
    }

    private fun loadBatteryInfo() {
        val design = batteryManager.getDesignCapacity()
        lateinit var engine: CalcBatteryAdvanced
        engine = CalcBatteryAdvanced(requireContext())
        val health2 = engine.getHealth(design)

        val estimated = CalcBatteryBasic.getMeasuredCapacityWhenFull(requireContext())

        val health = calcHealth(requireContext(),design)


        if (design != null) {
            binding.layoutTabHealth.txvInfoDesignBattery.text =
                "$design ${context?.getString(R.string.mAh)}"
        } else {
            binding.layoutTabHealth.txvInfoDesignBattery.text =
                "${context?.getString(R.string.nulll)}"
        }

        if (estimated != null) {
            binding.layoutTabHealth.txvInfoEstimateBattery.text =
                "$estimated  ${context?.getString(R.string.mAh)}"
        } else {
            binding.layoutTabHealth.txvInfoEstimateBattery.text =
                "${context?.getString(R.string.nulll)}"
        }


        if (health != null) {
            binding.layoutTabHealth.txvInfoBetteryPermission.text =
                "$health ${context?.getString(R.string.mAh)}"
        } else {
            binding.layoutTabHealth.txvInfoBetteryPermission.text =
                "${context?.getString(R.string.nulll)}"
        }



        updateNotDetected()
    }





    private fun showLoadingDialog(): AlertDialog {

        val view = layoutInflater.inflate(
            R.layout.layout_dialog_scan_battery,
            null
        )

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(false)
            .create()

        dialog.show()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }

    private fun showInputCapacityDialog() {

        val dialogBinding =
            LayoutDialogSetBatteryBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnSetBattery.setOnClickListener {

            val value = dialogBinding.edtCapacity.text.toString().toIntOrNull()

            if (value == null || value < 500) {
                dialogBinding.edtCapacity.error = "${context?.getString(R.string.invalid_capacity)}"
                return@setOnClickListener
            }

            // lưu
            batteryManager.saveDesignCapacity(value)

            // cập nhật UI
            binding.layoutTabHealth.txvInfoDesignBattery.text = "$value ${context?.getString(R.string.mAh)}"

            dialog.dismiss()

            // hiện loading animation
            val loadingDialog = showLoadingDialog()

            Handler(Looper.getMainLooper()).postDelayed({

                loadingDialog.dismiss()

            },1500)
        }
    }
    private fun startBatteryScan() {
        val design = batteryManager.getDesignCapacity()
        lateinit var engine: CalcBatteryAdvanced
        engine = CalcBatteryAdvanced(requireContext())
        val health2 = engine.getHealth(design)

        val health = calcHealth(requireContext(),design)

        val estimated = CalcBatteryBasic.getMeasuredCapacityWhenFull(requireContext())


        if (design == 0){
            Toast.makeText(
                requireContext(),
                "${context?.getString(R.string.set_battery_capacity_first)}",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val loadingDialog = showLoadingDialog()

        Handler(Looper.getMainLooper()).postDelayed({

            loadingDialog.dismiss()

            startActivity(
                Intent(requireContext(), ResultActivity::class.java).apply {
                   putExtra("battery_health", estimated)
                   putExtra("battery_design", design)
                }
            )

        },1500)
    }

    private fun updateNotDetected() {

        val scanned = batteryManager.isScanDone()

        binding.layoutTabHealth.vHealth.visibility =
            if (scanned) View.GONE else View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        loadBatteryInfo()
    }
}