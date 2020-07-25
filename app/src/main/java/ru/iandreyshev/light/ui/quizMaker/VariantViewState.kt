package ru.iandreyshev.light.ui.quizMaker

import ru.iandreyshev.light.domain.quizMaker.VariantId

sealed class VariantViewState {
    object AddNew : VariantViewState()
    data class Text(
        var id: VariantId? = null,
        var text: String = "",
        var isValid: Boolean,
        val position: Int,
        val onTextChanged: (String) -> Unit,
        val onValidStateChanged: (Boolean) -> Unit,
        val onDeleteVariant: () -> Unit
    ) : VariantViewState()
}
