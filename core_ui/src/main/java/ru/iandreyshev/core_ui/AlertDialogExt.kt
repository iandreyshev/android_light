package ru.iandreyshev.core_ui

import androidx.appcompat.app.AlertDialog

fun AlertDialog?.dismissOnDestroy() {
    this?.setOnDismissListener(null)
    this?.dismiss()
}
