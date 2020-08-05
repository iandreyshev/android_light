package ru.iandreyshev.constructor.domain.quiz.draft

import ru.iandreyshev.constructor.domain.quiz.QuestionId

data class QuestionDraft(
    val id: QuestionId,
    var text: String,
    var isMultipleMode: Boolean,
    val variants: MutableList<VariantDraft>
)
