package ru.iandreyshev.player_core.quiz

data class Question(
    val id: QuestionId,
    val text: String,
    val position: Int,
    val isMultipleMode: Boolean,
    val variants: List<Variant>,
    val result: QuestionResult?
)
