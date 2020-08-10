package ru.iandreyshev.constructor.ui.quizMaker

import ru.iandreyshev.constructor.domain.quiz.VariantDraftId

sealed class VariantViewState {
    data class NewVariantButton(
        val isFirstInBlock: Boolean
    ) : VariantViewState()

    data class Text(
        var id: VariantDraftId? = null,
        val text: String = "",
        val position: Int,
        val isCorrect: Boolean,
        val isFirstInBlock: Boolean,
        val isMultipleMode: Boolean,
        val canDelete: Boolean
    ) : VariantViewState()
}
