package ru.iandreyshev.constructor.domain.quiz

class Question(
    val id: QuestionDraftId,
    val text: String,
    val isMultipleMode: Boolean,
    val variants: List<Variant>
)
