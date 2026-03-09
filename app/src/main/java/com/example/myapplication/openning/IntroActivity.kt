package com.example.myapplication.openning

import com.dino.ads.onboading.BaseIntroActivity
import com.dino.ads.remote.NativeHolder
import com.dino.ads.remote.NativeMultiHolder
import com.dino.rate.replaceActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.RemoteConfig
import com.example.myapplication.ads.AdsManager
import com.example.myapplication.ui.HomeActivity

class IntroActivity : BaseIntroActivity() {

    override var nativeIntro: NativeMultiHolder = RemoteConfig.NATIVE_INTRO
    override var nativeIntroFull: NativeHolder = RemoteConfig.NATIVE_INTRO_FULL

    override fun nextActivity() {
        AdsManager.loadAndShowInter(this, RemoteConfig.INTER_INTRO) {
            replaceActivity<HomeActivity>()
        }
    }
}