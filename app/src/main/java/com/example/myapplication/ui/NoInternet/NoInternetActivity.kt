package com.example.myapplication.ui.NoInternet

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.databinding.ActivityNoInternetBinding
import com.example.myapplication.openning.SplashActivity
import com.example.myapplication.utils.CheckInternet

class NoInternetActivity : BaseActivity<ActivityNoInternetBinding>(
    inflater = ActivityNoInternetBinding::inflate
) {


    override fun initViewBase() {
        super.initViewBase()

        binding.tvRetry.setOnClickListener {
            if (CheckInternet.haveNetworkConnection(this)) {
                startActivity(Intent(this, SplashActivity::class.java))
            } else {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(intent)
            }
            finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        if (CheckInternet.haveNetworkConnection(this)) {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
    }

}