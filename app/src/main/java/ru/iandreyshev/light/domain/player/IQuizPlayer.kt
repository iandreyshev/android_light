package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.domain.player.quiz.Question

interface IQuizPlayer {
    fun isComplete(): Boolean
    fun applyAnswer(): Question
}
