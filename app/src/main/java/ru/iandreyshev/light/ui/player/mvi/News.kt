package ru.iandreyshev.light.ui.player.mvi

sealed class News {
    class ToastNews(val text: String) : News()
}
