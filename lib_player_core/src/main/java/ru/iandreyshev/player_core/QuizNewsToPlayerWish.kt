package ru.iandreyshev.player_core

import ru.iandreyshev.player_core.player.Wish
import ru.iandreyshev.player_core.quiz.News

internal class QuizNewsToPlayerWish : (News) -> Wish {

    override fun invoke(news: News) = when (news) {
        News.QuizFinished -> Wish.Forward
    }

}
