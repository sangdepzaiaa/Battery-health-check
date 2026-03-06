package com.example.myapplication.ads

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.dino.ads.admob.AdmobUtils
import com.dino.ads.remote.BannerHolder
import com.dino.ads.remote.InterHolder
import com.dino.ads.remote.NativeHolder
import com.dino.ads.remote.RewardHolder
import com.example.myapplication.RemoteConfig

object AdsManager {
    var isDebug = true

    fun loadNativeLanguage(context: Context) {
        AdmobUtils.loadNativeLanguage(context, RemoteConfig.NATIVE_LANGUAGE, object : AdmobUtils.NativeCallback() {})
        AdmobUtils.loadNative(context, RemoteConfig.NATIVE_SMALL_LANGUAGE, object : AdmobUtils.NativeCallback() {})
    }

    fun loadAndShowInter(activity: AppCompatActivity, holder: InterHolder, onFinished: () -> Unit) {
        AdmobUtils.loadAndShowInterstitial(activity, holder) { onFinished() }
    }

    fun loadAndShowBanner(activity: AppCompatActivity, holder: BannerHolder, viewGroup: ViewGroup) {
        AdmobUtils.loadAndShowBanner(
            activity, holder, viewGroup,
            object : AdmobUtils.BannerCallback() {},
            object : AdmobUtils.NativeCallback() {})
    }

    fun loadAndShowNative(activity: AppCompatActivity, holder: NativeHolder, viewGroup: ViewGroup) {
        AdmobUtils.loadAndShowNative(activity, holder, viewGroup, object : AdmobUtils.NativeCallback() {})
    }

    fun loadNativeFullscreen(context: Context, holder: NativeHolder, viewGroup: ViewGroup) {
        AdmobUtils.loadNativeFull(context, holder, object : AdmobUtils.NativeCallback() {})
    }

    fun loadAndShowReward(activity: AppCompatActivity, holder: RewardHolder,  viewGroup: ViewGroup) {
        AdmobUtils.loadAndShowReward(activity, holder,object : AdmobUtils.RewardCallback() {
            override fun onRewardClosed() {

            }

            override fun onRewardEarned() {

            }
        })
    }
}