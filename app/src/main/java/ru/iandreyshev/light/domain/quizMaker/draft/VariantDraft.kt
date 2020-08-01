package ru.iandreyshev.light.domain.quizMaker.draft

import ru.iandreyshev.light.domain.quizMaker.VariantId

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
