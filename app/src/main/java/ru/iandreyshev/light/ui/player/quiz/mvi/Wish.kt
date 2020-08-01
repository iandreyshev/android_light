package ru.iandreyshev.light.ui.player.quiz.mvi

sealed class Wish {
    class SwitchVariantValidState(val variantPosition: Int) : Wish()
    object Submit : Wish()
}
