package ru.iandreyshev.player_core.player

typealias PlayerWish = Wish

sealed class Wish {
    object Start : Wish()
    object Forward : Wish()
    object Back : Wish()
    object Repeat : Wish()
    object ShowError : Wish()
    object Exit : Wish()
}
