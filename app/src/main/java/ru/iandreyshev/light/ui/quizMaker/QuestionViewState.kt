package ru.iandreyshev.light.ui.quizMaker

import ru.iandreyshev.light.domain.quizMaker.QuestionId

data class QuestionViewState(
    var id: QuestionId? = null,
    var text: String = "",
    val variants: MutableList<VariantViewState>
)
