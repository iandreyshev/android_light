package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.player.PlayerItem

sealed class News {
    class ToastNews(val text: String) : News()
    class ShowQuiz(val quiz: PlayerItem.Quiz) : News()
}
