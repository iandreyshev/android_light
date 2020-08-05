package ru.iandreyshev.core_ui

import android.widget.PopupMenu

fun PopupMenu.dismissOnDestroy() {
    setOnDismissListener(null)
    dismiss()
}
