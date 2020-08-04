package ru.iandreyshev.light.domain.course

import ru.iandreyshev.light.domain.editor.CourseDraft
import ru.iandreyshev.light.domain.editor.DraftItem
import ru.iandreyshev.light.domain.editor.ISaveCourseDraftUseCase
import ru.iandreyshev.light.domain.quizMaker.Question
import ru.iandreyshev.light.domain.quizMaker.QuestionId
import ru.iandreyshev.light.domain.quizMaker.Variant
import ru.iandreyshev.light.domain.quizMaker.VariantId
import java.lang.IllegalArgumentException
import java.util.*

class SaveDraftUseCase(
    private val courseRepository: ICourseRepository
) : ISaveCourseDraftUseCase {

    override suspend fun invoke(draft: CourseDraft) {
        val course = Course(
            id = CourseId(Date().toString()),
            title = draft.title,
            creationDate = Date(),
            items = draft.items.map { item ->
                when (item) {
                    is DraftItem.Quiz ->
                        CourseItem.Quiz(
                            item.draft.questions
                                .map { questionDraft ->
                                    Question(
                                        id = QuestionId(Date().toString()),
                                        text = questionDraft.text,
                                        variants = questionDraft.variants.map { variantDraft ->
                                            Variant(
                                                id = VariantId(Date().toString()),
                                                text = variantDraft.text,
                                                isValid = variantDraft.isValid
                                            )
                                        },
                                        isMultipleMode = questionDraft.isMultipleMode
                                    )
                                })
                    is DraftItem.Image ->
                        CourseItem.Image(
                            item.draft.text,
                            item.draft.source
                                ?: throw IllegalArgumentException("Image draft source is null")
                        )
                    is DraftItem.Video ->
                        CourseItem.Video(
                            item.draft.source
                                ?: throw IllegalArgumentException("Video draft source is null")
                        )
                }
            }
        )

        courseRepository.save(course)
    }

}
