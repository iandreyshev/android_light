package ru.iandreyshev.light.domain.quizMaker

import ru.iandreyshev.light.domain.quizMaker.draft.QuizMakerDraft

interface ISaveDraftUseCase {
    suspend operator fun invoke(draft: QuizMakerDraft)
}
