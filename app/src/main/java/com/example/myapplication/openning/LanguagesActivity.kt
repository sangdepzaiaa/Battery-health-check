package com.example.myapplication.openning

import com.dino.ads.onboading.BaseLanguageActivity
import com.dino.ads.remote.NativeHolder
import com.dino.ads.remote.NativeMultiHolder
import com.dino.rate.replaceActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.RemoteConfig
import com.example.myapplication.ads.AdsManager

class LanguagesActivity : BaseLanguageActivity() {

    override var nativeLanguage: NativeMultiHolder = RemoteConfig.NATIVE_LANGUAGE
    override var nativeSmall: NativeHolder = RemoteConfig.NATIVE_SMALL_LANGUAGE
    override var nativeFull: NativeHolder = RemoteConfig.NATIVE_INTRO_FULL
    override var nativeIntro: NativeMultiHolder = RemoteConfig.NATIVE_INTRO
    override fun nextActivity() {
        AdsManager.loadAndShowInter(this, RemoteConfig.INTER_LANGUAGE) {
            if (fromSplash || uninstall) {
                replaceActivity<IntroActivity>()
            } else {
                replaceActivity<MainActivity>()
            }
        }

    }
}