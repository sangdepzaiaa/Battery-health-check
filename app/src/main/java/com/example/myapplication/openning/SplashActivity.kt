package com.example.myapplication.openning

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.dino.ads.admob.AdmobUtils
import com.dino.ads.admob.RemoteUtils
import com.dino.ads.iap.IapConfig
import com.dino.ads.iap.IapListener
import com.dino.ads.iap.IapUtils
import com.dino.ads.remote.NativeMultiHolder
import com.dino.rate.addActivity
import com.dino.rate.invisible
import com.example.myapplication.R
import com.example.myapplication.RemoteConfig
import com.example.myapplication.ads.AdsManager
import com.example.myapplication.ads.Common
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.ui.home.HomeActivity
import com.example.myapplication.ui.uninstall.ConfirmUninstallActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    private val handler = Handler(Looper.getMainLooper())
    var splash: String? = null

    override fun initViewBase() {
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action == Intent.ACTION_MAIN
        ) {
            finish()
            return
        }
        splash = intent.data?.getQueryParameter("splash") ?: intent.getStringExtra("splash")
        Common.onAppOpen(this)

        initData()

    }

    private fun initData() {
        RemoteUtils.init(this, R.xml.remote_config_defaults, null) {
            AdmobUtils.setupCMP(this) {
                AdmobUtils.initAdmob(this, AdsManager.isDebug)
                showInterOrAoa()
                AdsManager.loadNativeLanguage(this)
            }
        }
    }

    private fun showInterOrAoa() {
        AdmobUtils.loadAndShowAdSplash(
            this, RemoteConfig.ADS_SPLASH,
            object : AdmobUtils.InterCallback() {
                override fun onInterClosed() {
                    nextActivity()
                }

                override fun onInterFailed(error: String) {
                    binding.tvLoading.invisible()
                    handler.postDelayed({ nextActivity() }, 3000)
                }
            })
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }

    override fun callBackPress() {
    }

    private fun nextActivity() {
        if (!isFinishing) {
            if (intent.action != Intent.ACTION_VIEW) {
                startActivity(Intent(this, LanguagesActivity::class.java).apply {
                    putExtra("fromSplash", true)
                })
                finish()
                return
            }
            if (splash == "uninstall") {
                startActivity(Intent(this, ConfirmUninstallActivity::class.java))
                finish()
            }

        }
    }

}