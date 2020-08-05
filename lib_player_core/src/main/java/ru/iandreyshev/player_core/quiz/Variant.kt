package ru.iandreyshev.player_core.quiz

data class Variant(
    val id: VariantId,
    val text: String,
    val isSelectedAsValid: Boolean,
    val isValid: Boolean
)
