package ru.iandreyshev.light.domain.player.quiz

data class Question(
    val isComplete: Boolean,
    val text: String,
    val isMultipleMode: Boolean,
    val variants: List<Variant>
)
