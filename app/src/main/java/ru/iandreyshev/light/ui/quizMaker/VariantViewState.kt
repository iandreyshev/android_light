package ru.iandreyshev.light.ui.quizMaker

import ru.iandreyshev.light.domain.quizMaker.VariantId

sealed class VariantViewState {
    object AddNew : VariantViewState()
    data class Text(
        var id: VariantId? = null,
        var text: String = "",
        var isValid: Boolean
    ) : VariantViewState()
}
