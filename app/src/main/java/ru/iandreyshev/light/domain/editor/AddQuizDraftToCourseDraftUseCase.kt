package ru.iandreyshev.light.domain.editor

import ru.iandreyshev.light.domain.quizMaker.ISaveDraftUseCase
import ru.iandreyshev.light.domain.quizMaker.draft.QuizMakerDraft

class AddQuizDraftToCourseDraftUseCase(
    private val courseDraft: CourseDraft
) : ISaveDraftUseCase {

    override suspend fun invoke(draft: QuizMakerDraft) {
        courseDraft.add(draft)
    }

}
