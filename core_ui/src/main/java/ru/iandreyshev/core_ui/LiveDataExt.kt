package ru.iandreyshev.core_ui

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.modify(modifier: T.() -> T) {
    postValue(value?.let(modifier))
}
