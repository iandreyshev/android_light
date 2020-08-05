package ru.iandreyshev.player_core.quiz

import ru.iandreyshev.player_core.player.News

class PlayerNewsToQuizWish : (News) -> QuizWish? {

    override fun invoke(news: News): QuizWish? {
        return when (news) {
            is News.ShowQuiz -> Wish.Start(
                news.quiz
            )
            else -> null
        }
    }

}