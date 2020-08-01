package ru.iandreyshev.light.ui.player.quiz.mvi

import ru.iandreyshev.light.ui.player.quiz.VariantItem

data class State(
    val question: String,
    val variants: List<VariantItem>
)
