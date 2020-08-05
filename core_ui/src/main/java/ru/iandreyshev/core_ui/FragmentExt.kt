package ru.iandreyshev.core_ui

import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.fragment.app.Fragment

fun Fragment.setFullScreen(isFullScreen: Boolean = true) {
    if (isFullScreen) {
        activity?.window?.addFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    } else {
        activity?.window?.clearFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}

fun Fragment.setOrientationPortrait() {
    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun Fragment.setOrientationUnspecified() {
    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}
