package ru.iandreyshev.light.domain.course

import ru.iandreyshev.light.domain.editor.CourseDraft
import ru.iandreyshev.light.domain.editor.DraftItem
import ru.iandreyshev.light.domain.editor.ISaveCourseDraftUseCase
import ru.iandreyshev.light.domain.imageMaker.Image
import ru.iandreyshev.light.domain.imageMaker.ImageSource
import ru.iandreyshev.light.domain.quizMaker.Quiz
import ru.iandreyshev.light.domain.videoMaker.Video
import ru.iandreyshev.light.domain.videoMaker.VideoSource
import java.util.*

class SaveDraftUseCase(
    private val courseRepository: ICourseRepository
) : ISaveCourseDraftUseCase {

    override suspend fun invoke(draft: CourseDraft) {
        val course = Course(
            id = CourseId(""),
            title = draft.title,
            creationDate = Date(),
            items = draft.items.map { item ->
                when (item) {
                    is DraftItem.Quiz ->
                        CourseItem.Quiz(Quiz(listOf()))
                    is DraftItem.Image ->
                        CourseItem.Image(Image("", item.draft.imageSource!!))
                    is DraftItem.Video ->
                        CourseItem.Video(Video("", VideoSource("")))
                }
            }
        )

        courseRepository.save(course)
    }

}
