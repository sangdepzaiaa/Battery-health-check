package com.example.myapplication.ui.uninstall

import android.content.Intent
import com.dino.ads.onboading.uninstall.BaseUninstallActivity
import com.dino.ads.remote.NativeHolder
import com.example.myapplication.RemoteConfig
import com.example.myapplication.ads.AdsManager
import com.example.myapplication.openning.LanguagesActivity

class ConfirmUninstallActivity : BaseUninstallActivity() {
    override val activityBack: Class<*> = LanguagesActivity::class.java
    override var nativeUninstall: NativeHolder = RemoteConfig.NATIVE_UNINSTALL

    override fun nextActivity() {
        AdsManager.loadAndShowInter(this, RemoteConfig.INTER_UNINSTALL) {
            startActivity(Intent(this, UninstallSurveyActivity::class.java))
        }
    }
}