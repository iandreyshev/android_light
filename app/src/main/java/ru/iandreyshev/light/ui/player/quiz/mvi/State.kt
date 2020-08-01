package ru.iandreyshev.light.ui.player.quiz.mvi

import ru.iandreyshev.light.domain.player.quiz.QuestionResult
import ru.iandreyshev.light.domain.player.quiz.QuizResult
import ru.iandreyshev.light.domain.player.quiz.Variant

data class State(
    val type: Type = Type.QUESTION,
    val questionText: String = "",
    val questionIndex: Int = 0,
    val questionsCount: Int = 0,
    val variants: List<Variant> = listOf(),
    val isMultipleMode: Boolean = false,
    val questionResult: QuestionResult? = null,
    val quizResult: QuizResult? = null
) {

    fun hasNext() = questionIndex + 1 < questionsCount

    enum class Type {
        QUESTION,
        RESULTS;
    }

}
