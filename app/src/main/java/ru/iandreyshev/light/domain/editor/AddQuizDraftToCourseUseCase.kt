package ru.iandreyshev.light.domain.editor

import ru.iandreyshev.light.domain.quizMaker.ISaveQuizDraftUseCase
import ru.iandreyshev.light.domain.quizMaker.draft.QuizDraft

class AddQuizDraftToCourseUseCase(
    private val courseDraft: CourseDraft
) : ISaveQuizDraftUseCase {

    override suspend fun invoke(draft: QuizDraft) {
        courseDraft.add(draft)
    }

}
