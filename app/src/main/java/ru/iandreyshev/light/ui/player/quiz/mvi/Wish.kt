package ru.iandreyshev.light.ui.player.quiz.mvi

import ru.iandreyshev.light.domain.player.PlayerItem

typealias QuizWish = Wish

sealed class Wish {
    class Start(val quiz: PlayerItem.Quiz) : Wish()
    class SwitchVariantValidState(val variantPosition: Int) : Wish()
    object Submit : Wish()
}
