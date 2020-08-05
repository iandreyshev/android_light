package ru.iandreyshev.light.domain

import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.editor.ISaveCourseDraftUseCase
import ru.iandreyshev.constructor.domain.image.draft.ImageSource
import ru.iandreyshev.constructor.domain.videoMaker.VideoSource
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId
import ru.iandreyshev.player_core.course.CourseItem
import ru.iandreyshev.player_core.course.ICourseRepository
import ru.iandreyshev.player_core.quiz.Question
import ru.iandreyshev.player_core.quiz.QuestionId
import ru.iandreyshev.player_core.quiz.Variant
import ru.iandreyshev.player_core.quiz.VariantId
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
                                .mapIndexed { pos, questionDraft ->
                                    Question(
                                        id = QuestionId(Date().toString()),
                                        text = questionDraft.text,
                                        variants = questionDraft.variants.map { variantDraft ->
                                            Variant(
                                                id = VariantId(Date().toString()),
                                                text = variantDraft.text,
                                                isValid = variantDraft.isValid,
                                                isSelectedAsValid = true
                                            )
                                        },
                                        isMultipleMode = questionDraft.isMultipleMode,
                                        position = pos,
                                        result = null
                                    )
                                })
                    is DraftItem.Image ->
                        CourseItem.Image(
                            item.draft.text,
                            item.draft.source
                                ?.asPlayerImageSource()
                                ?: throw IllegalArgumentException("Image draft source is null")
                        )
                    is DraftItem.Video ->
                        CourseItem.Video(
                            item.draft.source
                                ?.asPlayerVideoSource()
                                ?: throw IllegalArgumentException("Video draft source is null")
                        )
                }
            }
        )

        courseRepository.save(course)
    }

    private fun ImageSource.asPlayerImageSource() =
        ru.iandreyshev.player_core.image.ImageSource(filePath)

    private fun VideoSource.asPlayerVideoSource() =
        ru.iandreyshev.player_core.video.VideoSource(filePath)

}
