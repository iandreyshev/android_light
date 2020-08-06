package ru.iandreyshev.constructor.domain.quiz

import ru.iandreyshev.constructor.domain.quiz.draft.QuizDraft

interface ISaveQuizDraftUseCase {
    suspend operator fun invoke(draft: QuizDraft)
}
