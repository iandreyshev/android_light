package ru.iandreyshev.player_core

import ru.iandreyshev.player_core.player.News
import ru.iandreyshev.player_core.quiz.QuizWish
import ru.iandreyshev.player_core.quiz.Wish

internal class PlayerNewsToQuizWish : (News) -> QuizWish? {

    override fun invoke(news: News): QuizWish? {
        return when (news) {
            is News.ShowQuiz -> Wish.Start(
                news.quiz
            )
            else -> null
        }
    }

}