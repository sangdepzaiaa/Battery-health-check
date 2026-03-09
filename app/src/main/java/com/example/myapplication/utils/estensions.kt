package com.example.myapplication.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.View
import com.example.myapplication.ui.NoInternet.NoInternetActivity

fun View.tap(action: (view: View?) -> Unit) {
    setOnClickListener(object : TapListener() {
        override fun onTap(v: View?) {
            if (!CheckInternet.haveNetworkConnection(context)) {
                context.findActivity()?.let {
                    val intent = Intent(it, NoInternetActivity::class.java)
                    it.startActivity(intent)
                    //confirm fix bug here
                    it.overridePendingTransition(0, 0)
                }
                return
            }
            action(v)
        }
    })
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

fun Activity.navigateTo(targetClass: Class<out Activity>) {
    val currentClass = this::class.java

    // Prevent navigating to the same screen (avoids flicker & unnecessary work)
    if (currentClass == targetClass) {
        return
    }

    val intent = Intent(this, targetClass)

    startActivity(intent)

    // Most popular & smooth animation for bottom navigation bar pattern
    overridePendingTransition(
        android.R.anim.slide_in_left,     // new screen slides in from right
        android.R.anim.slide_out_right    // old screen slides out to left
    )

    finish()

    // Alternative smooth options (uncomment if you prefer):

    // Fade transition - very clean & modern
    // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    // No animation - fastest but can feel abrupt
    // overridePendingTransition(0, 0)
}