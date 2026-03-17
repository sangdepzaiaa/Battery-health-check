package com.example.myapplication.ui.uninstall

import com.dino.ads.onboading.uninstall.BaseUninstallSurveyActivity
import com.dino.ads.remote.NativeHolder
import com.example.myapplication.RemoteConfig
import com.example.myapplication.ads.AdsManager
import com.example.myapplication.openning.LanguagesActivity

class UninstallSurveyActivity : BaseUninstallSurveyActivity() {
    override var nativeUninstallSurvey: NativeHolder = RemoteConfig.NATIVE_UNINSTALL_SURVEY
    override val activityBack: Class<*> = LanguagesActivity::class.java

    override fun openUninstall() {
        AdsManager.loadAndShowInter(this, RemoteConfig.INTER_UNINSTALL_SURVEY) {
            super.openUninstall()
        }
    }
}