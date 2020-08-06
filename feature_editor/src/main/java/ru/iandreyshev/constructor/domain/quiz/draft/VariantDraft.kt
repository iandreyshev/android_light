package ru.iandreyshev.constructor.domain.quiz.draft

import ru.iandreyshev.constructor.domain.quiz.VariantId

data class VariantDraft(
    val id: VariantId,
    var text: String,
    var isValid: Boolean
) {

    companion object {
        fun empty() = VariantDraft(
            id = VariantId(""),
            text = "",
            isValid = false
        )
    }

}
