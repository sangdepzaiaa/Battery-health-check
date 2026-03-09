package com.example.myapplication.base

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.dino.ads.admob.RemoteUtils

abstract class BaseActivity<VB : ViewBinding>(val inflater: (LayoutInflater) -> VB) :
    AppCompatActivity() {

    val binding: VB by lazy { inflater(layoutInflater) }
    private lateinit var connectivityManager: ConnectivityManager
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        applyImmersiveMode()
        initViewBase()
        onBackPressedDispatcher.addCallback(this) {
            callBackPress()
        }
        registerNetworkCallback()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) applyImmersiveMode()
    }

    private fun applyImmersiveMode() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    open fun initViewBase(){}

    open fun callBackPress() {
        finish()
    }

    private fun registerNetworkCallback() {
        connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread {
                    runCatching {
                        dialog?.dismiss()
                    }
                    onNetworkAvailable()
                }
            }

            override fun onLost(network: Network) {
                runOnUiThread {
                    showNoInternetDialog()
                    onNetworkLost()
                }
            }
        }

        connectivityManager.registerNetworkCallback(request, networkCallback!!)
    }

    open fun onNetworkAvailable() {}

    open fun onNetworkLost() {}

    private fun showNoInternetDialog() {
        runCatching {
            if (dialog == null) {
                dialog = RemoteUtils.dialogNoInternet(this) {
                    dialog?.show()
                }
                dialog?.setOnDismissListener {
                    dialog?.dismiss()
                }
                dialog?.setCancelable(false)
            }

            if (dialog?.isShowing != true) {
                dialog?.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
        }
    }
}