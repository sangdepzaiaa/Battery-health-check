package com.example.myapplication.base

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.dino.ads.admob.RemoteUtils
import com.example.myapplication.ui.NoInternet.NoInternetActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseActivity<VB: ViewBinding>(var inflater: (LayoutInflater) -> VB) : AppCompatActivity(){
    protected val binding:VB by lazy{ inflater(layoutInflater)}

    lateinit var connectivityManager: ConnectivityManager
    lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private var dialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()

        onBackPressedDispatcher.addCallback(this) {
            callBackPress()
        }

        setPaddingg()
        setNetwork()
        hideSystembar()

        initViewBase()
        bindView()
        bindViewmodel()
    }

    open fun callBackPress() {
        finish()
    }

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


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystembar()
    }

    open fun hideSystembar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            WindowInsetsControllerCompat(window ,binding.root).apply {
                WindowCompat.setDecorFitsSystemWindows(window,false)
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }else{
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    open fun setPaddingg(){
        ViewCompat.setOnApplyWindowInsetsListener(binding.root){v,inset ->
            val sys = inset.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            val ime = inset.getInsets(WindowInsetsCompat.Type.ime()).bottom
            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                maxOf(sys,ime)
            )
            inset
        }
    }

    open fun setNetwork(){
        connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback(){
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val realInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                lifecycleScope.launch(Dispatchers.Main) {
                    if (realInternet){
                    //    if (this@BaseActivity is NoInternetActivity) finish()
                        dialog?.dismiss()
                    }else{
                        //startActivity(Intent(this@BaseActivity, NoInternetActivity::class.java))
                        showNoInternetDialog()
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                lifecycleScope.launch(Dispatchers.Main) {
//                    if (this@BaseActivity !is NoInternetActivity){
//                        startActivity(Intent(this@BaseActivity, NoInternetActivity::class.java))
//                    }
                    showNoInternetDialog()
                }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }



    open fun initViewBase(){}
    open fun bindView(){}
    open fun bindViewmodel(){}

}