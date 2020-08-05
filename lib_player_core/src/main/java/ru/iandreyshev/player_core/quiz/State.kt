package ru.iandreyshev.player_core.quiz

typealias QuizPlayerState = State

data class State(
    val type: Type = Type.DISABLED,
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
        DISABLED,
        PREVIEW,
        QUESTION,
        RESULTS;
    }

}
