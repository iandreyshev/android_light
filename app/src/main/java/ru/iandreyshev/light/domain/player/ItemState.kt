package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.domain.player.quiz.Question

sealed class ItemState {
    class Image(
        val uri: String
    ) : ItemState()

    class Quiz(
        val question: Question,
        val questionsCount: Int,
        val questionsCompleteCount: Int
    ) : ItemState()
}
