package ru.iandreyshev.light.domain.quizMaker

class Question(
    val id: QuestionId,
    val text: String,
    val variants: List<Variant>
)
