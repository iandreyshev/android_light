package ru.iandreyshev.light.domain.player.quiz

data class Question(
    val text: String,
    val isMultipleMode: Boolean,
    val variants: List<Variant>,
    val result: QuestionResult?
)
