package ru.iandreyshev.light.domain.quizMaker

import ru.iandreyshev.light.domain.quizMaker.draft.QuizDraft

interface ISaveQuizDraftUseCase {
    suspend operator fun invoke(draft: QuizDraft)
}
