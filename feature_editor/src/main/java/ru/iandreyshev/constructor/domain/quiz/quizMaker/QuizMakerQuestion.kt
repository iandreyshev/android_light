package ru.iandreyshev.constructor.domain.quiz.quizMaker

import ru.iandreyshev.constructor.domain.quiz.QuestionDraftId

data class QuizMakerQuestion(
    val id: QuestionDraftId,
    var text: String,
    var isMultipleMode: Boolean,
    val variants: MutableList<QuizMakerVariant>
)
