package ru.iandreyshev.light.ui.player.mvi

sealed class Wish {
    object Start : Wish()
    object Forward : Wish()
    object Back : Wish()
    object ApplyAnswer : Wish()
    object Repeat : Wish()
}
