package ru.iandreyshev.constructor.domain.quiz.draft

import ru.iandreyshev.constructor.domain.quiz.VariantDraftId

data class VariantDraft(
    val id: VariantDraftId,
    val text: String,
    val isCorrect: Boolean
)
