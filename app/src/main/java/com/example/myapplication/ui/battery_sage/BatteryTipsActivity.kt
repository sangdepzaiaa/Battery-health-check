package com.example.myapplication.ui.battery_sage

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.data.BatteryTipModel
import com.example.myapplication.databinding.ActivityBatteryTipsBinding
import com.example.myapplication.ui.battery_sage.adapter.BatteryTipAdapter
import com.example.myapplication.utils.tap

class BatteryTipsActivity : BaseActivity<ActivityBatteryTipsBinding>(
    inflater = ActivityBatteryTipsBinding::inflate
) {
    override fun initViewBase() {
        super.initViewBase()
        setupView()
    }

    private fun setupView(){

        binding.layoutToolBarBatteryTip.btnBack.tap {
            finish()
        }

        val tips = listOf(
            BatteryTipModel(
                R.drawable.ic_temperature,
                getString(R.string.Keep_it_cool),
                getString(R.string.text_battery_tip_1)
            ),
            BatteryTipModel(
                R.drawable.ic_info,
                getString(R.string.charging_habits),
                getString(R.string.text_battery_tip_2)
            ),
            BatteryTipModel(
                R.drawable.ic_battery_3,
                getString(R.string.battery_usage),
                getString(R.string.text_battery_tip_3)
            ),
            BatteryTipModel(
                R.drawable.ic_battery_2,
                getString(R.string.conserve_battery_when_low),
                getString(R.string.text_battery_tip_4)
            ),
            BatteryTipModel (
                    R.drawable.ic_battery_1,
                getString(R.string.fast_charge_sparingly),
                getString(R.string.text_battery_tip_5)
            )
        )

        binding.rcv.layoutManager = LinearLayoutManager(this@BatteryTipsActivity)
        binding.rcv.adapter = BatteryTipAdapter(tips)
    }
}