package ru.iandreyshev.light.ui.player.quiz.mvi

import ru.iandreyshev.light.ui.player.mvi.News

class PlayerNewsToQuizWish : (News) -> QuizWish? {

    override fun invoke(news: News): QuizWish? {
        return when (news) {
            is News.ShowQuiz -> Wish.Start(news.quiz)
            else -> null
        }
    }

}