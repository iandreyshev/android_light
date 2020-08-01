package ru.iandreyshev.light.domain.player.quiz

interface IQuizPlayer {
    val currentQuestion: Question
    val currentQuestionPosition: Int
    val questionsCount: Int
    fun getQuizResult(): QuizResult
    fun moveToNextQuestion()
    fun switchCurrQuestionVariantValidState(variantPosition: Int)
    fun submitCurrentQuestion()
}
