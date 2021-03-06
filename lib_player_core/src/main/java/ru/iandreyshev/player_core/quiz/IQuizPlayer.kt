package ru.iandreyshev.player_core.quiz

import ru.iandreyshev.player_core.course.PlayerItem

interface IQuizPlayer {
    val title: String
    val currentQuestion: Question
    val currentQuestionPosition: Int
    val questionsCount: Int
    val result: QuizResult
    fun prepare(playerQuiz: PlayerItem.Quiz)
    fun moveToNextQuestion()
    fun switchCurrQuestionVariantCorrectState(variantPosition: Int)
    fun submitCurrentQuestion()
}
