package ru.iandreyshev.player_core.quiz

interface IQuizResultListener {
    fun onChange(quizId: QuizId, result: QuizResult)
}
