package ru.iandreyshev.constructor.domain.quiz.quizMaker

import ru.iandreyshev.constructor.domain.quiz.draft.QuizDraft

sealed class CreateResult {
    class Success(val draft: QuizDraft) : CreateResult()
    class ErrorInvalidQuestion(val position: Int) : CreateResult()
}
