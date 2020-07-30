package ru.iandreyshev.light.utill

import android.widget.PopupMenu

fun PopupMenu.dismissOnDestroy() {
    setOnDismissListener(null)
    dismiss()
}
