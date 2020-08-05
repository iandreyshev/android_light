package ru.iandreyshev.player_core.utils

import io.reactivex.Observable

fun <T> T.just() = Observable.just(this)
