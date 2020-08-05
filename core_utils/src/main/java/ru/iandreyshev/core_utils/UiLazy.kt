package ru.iandreyshev.core_utils

fun <T> uiLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)
