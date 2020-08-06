package ru.iandreyshev.constructor.domain.quiz

import ru.iandreyshev.constructor.domain.quiz.draft.QuizDraft

interface IQuizDraftRepository {
    suspend fun get(): QuizDraft
    suspend fun save(draft: QuizDraft)
}
