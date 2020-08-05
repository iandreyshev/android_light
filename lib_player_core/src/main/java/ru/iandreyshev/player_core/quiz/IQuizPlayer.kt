package ru.iandreyshev.player_core.quiz

import ru.iandreyshev.player_core.course.PlayerItem

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
