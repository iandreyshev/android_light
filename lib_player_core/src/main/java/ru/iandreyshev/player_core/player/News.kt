package ru.iandreyshev.player_core.player

import ru.iandreyshev.player_core.course.PlayerItem

sealed class News {
    class ToastNews(val text: String) : News()
    class ShowQuiz(val quiz: PlayerItem.Quiz) : News()
}
