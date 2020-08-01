package ru.iandreyshev.light.ui.player

import ru.iandreyshev.light.ui.player.mvi.Wish
import ru.iandreyshev.light.ui.player.quiz.mvi.News

class QuizNewsToPlayerWish : (News) -> Wish {

    override fun invoke(news: News) = when (news) {
        News.QuizFinished -> Wish.Forward
    }

}
