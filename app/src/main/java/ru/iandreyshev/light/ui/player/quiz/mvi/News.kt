package ru.iandreyshev.light.ui.player.quiz.mvi

sealed class News {
    object QuizFinished : News()
}
