package ru.iandreyshev.light.ui.player.mvi

sealed class Wish {
    object Start : Wish()
    object Forward : Wish()
    object Back : Wish()
    object Repeat : Wish()
    object ShowError : Wish()
}
