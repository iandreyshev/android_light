package ru.iandreyshev.light.utill

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <T> LiveData<T>.distinctUntilChanged() =
    Transformations.distinctUntilChanged(this)
