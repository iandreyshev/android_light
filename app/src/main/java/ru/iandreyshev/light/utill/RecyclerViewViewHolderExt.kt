package ru.iandreyshev.light.utill

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.safelyPosition(action: (Int) -> Unit) {
    when (val position = adapterPosition) {
        RecyclerView.NO_POSITION -> Unit
        else -> action(position)
    }
}