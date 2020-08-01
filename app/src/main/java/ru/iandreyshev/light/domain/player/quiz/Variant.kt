package ru.iandreyshev.light.domain.player.quiz

data class Variant(
    val text: String,
    val isSelectedAsValid: Boolean,
    val isValid: Boolean
)
