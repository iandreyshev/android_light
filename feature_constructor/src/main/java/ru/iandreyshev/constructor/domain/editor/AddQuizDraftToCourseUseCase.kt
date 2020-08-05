package ru.iandreyshev.constructor.domain.editor

import ru.iandreyshev.constructor.domain.quiz.ISaveQuizDraftUseCase
import ru.iandreyshev.constructor.domain.quiz.draft.QuizDraft

class AddQuizDraftToCourseUseCase(
    private val courseDraft: CourseDraft
) : ISaveQuizDraftUseCase {

    override suspend fun invoke(draft: QuizDraft) {
        courseDraft.add(draft)
    }

}
