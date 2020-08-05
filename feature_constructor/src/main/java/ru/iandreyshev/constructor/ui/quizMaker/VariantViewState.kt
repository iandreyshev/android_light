package ru.iandreyshev.constructor.ui.quizMaker

import ru.iandreyshev.constructor.domain.quiz.VariantId

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
