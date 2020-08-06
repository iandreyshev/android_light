package ru.iandreyshev.constructor.ui.quizMaker

import ru.iandreyshev.constructor.domain.quiz.QuestionId

data class QuestionViewState(
    var id: QuestionId,
    var text: String = ""
)
