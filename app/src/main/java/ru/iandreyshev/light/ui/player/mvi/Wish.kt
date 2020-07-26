package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.ui.player.UiAction

sealed class Wish {
    object Forward : Wish()
    object Back : Wish()
    object ApplyAnswer : Wish()
    object Exit : Wish()
}
