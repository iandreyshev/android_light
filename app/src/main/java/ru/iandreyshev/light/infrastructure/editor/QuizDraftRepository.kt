package ru.iandreyshev.light.infrastructure.editor

import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.quiz.IQuizDraftRepository
import ru.iandreyshev.constructor.domain.quiz.QuizDraftId
import ru.iandreyshev.constructor.domain.quiz.draft.QuizDraft
import ru.iandreyshev.constructor.utils.newUID

class QuizDraftRepository(
    private val id: QuizDraftId,
    private val courseRepository: ICourseDraftRepository
) : IQuizDraftRepository {

    override suspend fun get(): QuizDraft {
        return courseRepository.getItems()
            .mapNotNull { it.asTypeOrNull<DraftItem.Quiz>()?.draft }
            .firstOrNull { it.id == id }
            ?: QuizDraft(
                QuizDraftId(newUID()),
                listOf()
            )
    }

    override suspend fun save(draft: QuizDraft) {
        courseRepository.add(DraftItem.Quiz(draft))
    }

}
