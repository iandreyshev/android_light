package ru.iandreyshev.core_ui

import androidx.recyclerview.widget.RecyclerView

inline fun RecyclerView.ViewHolder.safelyPosition(action: (Int) -> Unit) {
    when (val position = adapterPosition) {
        RecyclerView.NO_POSITION -> Unit
        else -> action(position)
    }
}
