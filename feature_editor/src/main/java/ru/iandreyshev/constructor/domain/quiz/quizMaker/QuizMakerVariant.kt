package ru.iandreyshev.constructor.domain.quiz.quizMaker

import ru.iandreyshev.constructor.domain.quiz.VariantDraftId

data class QuizMakerVariant(
    val id: VariantDraftId,
    var text: String,
    var isCorrect: Boolean
) {

    companion object {
        fun empty() = QuizMakerVariant(
            id = VariantDraftId(""),
            text = "",
            isCorrect = false
        )
    }

}
