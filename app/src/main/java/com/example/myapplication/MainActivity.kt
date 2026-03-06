package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dino.ads.admob.AdmobUtils
import com.dino.rate.addActivity
import com.dino.rate.replaceActivity
import com.example.myapplication.ads.AdsManager
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.TestActivity

class MainActivity : BaseActivity<ActivityMainBinding>(
    inflater = ActivityMainBinding::inflate
) {
    override fun initViewBase() {
        binding.btnAdsInter.setOnClickListener {
            AdsManager.loadAndShowInter(this, RemoteConfig.INTER_HOME) {
                addActivity<TestActivity>()
            }
        }

        binding.btnAdsColap.setOnClickListener {
            AdsManager.loadNativeFullscreen(this, RemoteConfig.NATIVE_INTRO_FULL,binding.frEnd)
        }

        binding.btnAdsBanner.setOnClickListener {
            AdsManager.loadAndShowBanner(this, RemoteConfig.BANNER_HOME,binding.frEnd)
            AdsManager.loadAndShowBanner(this, RemoteConfig.BANNER_HOME.anchorTop(), binding.frEnd)
        }

        binding.btnAdsNative.setOnClickListener {
            AdsManager.loadAndShowNative(this, RemoteConfig.NATIVE_INTRO_FULL, binding.frEnd)
        }

        binding.btnAdsReward.setOnClickListener {
                AdmobUtils.loadAndShowReward(this, RemoteConfig.REWARD_TEST, object : AdmobUtils.RewardCallback() {
                    override fun onRewardClosed() {

                    }

                    override fun onRewardEarned() {
                        Toast.makeText(this@MainActivity,"Bạn đã nhận 10 coin",Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}