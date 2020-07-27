package ru.iandreyshev.light.ui.quizMaker

import ru.iandreyshev.light.domain.quizMaker.VariantId

sealed class VariantViewState {
    data class NewVariantButton(
        val isFirstInBlock: Boolean
    ) : VariantViewState()

    data class Text(
        var id: VariantId? = null,
        val text: String = "",
        val position: Int,
        val isValid: Boolean,
        val isFirstInBlock: Boolean,
        val isMultipleMode: Boolean
    ) : VariantViewState()
}
