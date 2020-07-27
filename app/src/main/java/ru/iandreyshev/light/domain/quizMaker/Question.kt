package ru.iandreyshev.light.domain.quizMaker

class Question(
    val id: QuestionId,
    val text: String,
    val isMultipleMode: Boolean,
    val variants: List<Variant>
)
