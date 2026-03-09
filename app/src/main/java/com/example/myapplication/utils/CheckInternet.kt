package com.example.myapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object CheckInternet {

    private const val TAG = "CheckInternetTag"
    private const val Event_TAG = "NetChk"

    private fun haveNetworkConnection2(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                true
            }
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                true
            }

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                true
            }

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                true
            }
            // else return false
            else -> {
                false
            }
        }

    }

    private fun haveNetworkConnection1(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val hasCapabilityInternet =
            (networkCapabilities != null && networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            ))

        return hasCapabilityInternet
    }

    fun haveNetworkConnection(context: Context): Boolean {

        val isInternet1 = haveNetworkConnection1(context)
        val isInternet2 = haveNetworkConnection2(context)
        val isInternet = isInternet1 || isInternet2

        return isInternet
    }
}
