package ru.iandreyshev.constructor.ui.quizMaker

import ru.iandreyshev.constructor.domain.quiz.QuestionDraftId

data class QuestionViewState(
    var id: QuestionDraftId,
    var text: String = ""
)
