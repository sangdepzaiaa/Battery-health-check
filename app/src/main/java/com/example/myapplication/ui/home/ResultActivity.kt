package com.example.myapplication.ui.home

import android.util.Log
import com.dino.ads.utils.log
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.databinding.ActivityResultBinding

class ResultActivity : BaseActivity<ActivityResultBinding>(
    inflater = ActivityResultBinding::inflate
) {
    private val health by lazy { intent.getIntExtra("battery_health", -1) }
    private val design by lazy { intent.getIntExtra("battery_design", 0) }
    private val context by lazy { this@ResultActivity }

    override fun initViewBase() {
        super.initViewBase()

        Log.d("ResultActivity", "health = $health , design = $design")

        binding.txvInfoEstimateBattery.text = "$health ${context.getString(R.string.mAh)}"
        binding.txvInfoBetteryPermission.text = "$design ${context.getString(R.string.mAh)}"

        if (health > 0 && design > 0) {
            val percent = (health * 100f) / design
            binding.batteryHealthView.setPercent(percent)
        }

        binding.btnOk.setOnClickListener {
            finish()
        }
    }
}