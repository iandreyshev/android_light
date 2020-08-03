package ru.iandreyshev.light.domain.player.quiz

import ru.iandreyshev.light.domain.player.PlayerItem

interface IQuizPlayer {
    val currentQuestion: Question
    val currentQuestionPosition: Int
    val questionsCount: Int
    val result: QuizResult?
    fun prepare(playerQuiz: PlayerItem.Quiz)
    fun moveToNextQuestion()
    fun switchCurrQuestionVariantValidState(variantPosition: Int)
    fun submitCurrentQuestion()
    fun onFinish()
}
