package ru.iandreyshev.core_ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <T> LiveData<T>.distinctUntilChanged() =
    Transformations.distinctUntilChanged(this)
