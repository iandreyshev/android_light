package ru.iandreyshev.constructor.domain.quiz.draft

import ru.iandreyshev.constructor.domain.quiz.QuestionDraftId

data class QuestionDraft(
    val id: QuestionDraftId,
    var text: String,
    var isMultipleMode: Boolean,
    val variants: MutableList<VariantDraft>
)
