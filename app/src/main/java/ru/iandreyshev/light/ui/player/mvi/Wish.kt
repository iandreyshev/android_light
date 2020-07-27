package ru.iandreyshev.light.ui.player.mvi

sealed class Wish {
    object Start : Wish()
    object Next : Wish()
    object Previous : Wish()
    object ApplyAnswer : Wish()
    object Exit : Wish()
}
