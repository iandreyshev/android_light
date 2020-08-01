package ru.iandreyshev.light.utill

import io.reactivex.Observable

fun <T> T.just() = Observable.just(this)
