package ru.iandreyshev.constructor.domain.quiz.draft

import ru.iandreyshev.constructor.domain.quiz.VariantDraftId

data class VariantDraft(
    val id: VariantDraftId,
    var text: String,
    var isValid: Boolean
) {

    companion object {
        fun empty() = VariantDraft(
            id = VariantDraftId(""),
            text = "",
            isValid = false
        )
    }

}
