package ru.iandreyshev.player_core.quiz

import ru.iandreyshev.player_core.course.PlayerItem

typealias QuizWish = Wish

sealed class Wish {
    class Start(val quiz: PlayerItem.Quiz) : Wish()
    class SwitchVariantCorrectState(val variantPosition: Int) : Wish()
    object Submit : Wish()
}
