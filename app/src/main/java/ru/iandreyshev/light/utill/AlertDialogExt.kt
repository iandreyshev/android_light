package ru.iandreyshev.light.utill

import androidx.appcompat.app.AlertDialog

fun AlertDialog?.dismissOnDestroy() {
    this?.setOnDismissListener(null)
    this?.dismiss()
}
