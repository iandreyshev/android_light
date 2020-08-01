package ru.iandreyshev.light.domain.quizMaker.draft

import ru.iandreyshev.light.domain.quizMaker.QuestionId

data class QuestionDraft(
    val id: QuestionId,
    var text: String,
    var isMultipleMode: Boolean,
    val variants: MutableList<VariantDraft>
)
