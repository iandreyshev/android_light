package ru.iandreyshev.player_core

import ru.iandreyshev.player_core.player.PlayerWish
import ru.iandreyshev.player_core.quiz.QuizWish

interface IWishListener {
    operator fun invoke(wish: PlayerWish) {}
    operator fun invoke(wish: QuizWish) {}
}
