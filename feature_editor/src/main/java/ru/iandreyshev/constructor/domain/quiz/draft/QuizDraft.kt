package ru.iandreyshev.constructor.domain.quiz.draft

import ru.iandreyshev.constructor.domain.quiz.QuizDraftId

data class QuizDraft(
    val id: QuizDraftId,
    val questions: List<QuestionDraft>
)
