package ru.iandreyshev.light.infrastructure.courseList

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.image.ImageSource
import ru.iandreyshev.constructor.domain.video.VideoSource
import ru.iandreyshev.light.domain.ICourseRepository
import ru.iandreyshev.light.domain.IDraftSerializer
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId
import ru.iandreyshev.player_core.course.CourseItem
import ru.iandreyshev.player_core.quiz.*
import java.util.*

class InMemoryCourseRepository : ICourseRepository, IDraftSerializer {

    private val mCourses = mutableListOf<Course>()
    private val mCoursesObservable = ConflatedBroadcastChannel<List<Course>>()

    override fun getCourse(id: CourseId): Course? {
        return mCourses.firstOrNull { it.id == id }
    }

    override fun getCoursesObservable(): Flow<List<Course>> =
        mCoursesObservable.asFlow()

    override fun save(draft: CourseDraft) {
        mCourses.add(draft.asCourse())
        mCoursesObservable.offer(mCourses)
    }

    override fun delete(courseId: CourseId) {
        mCourses.indexOfFirst { it.id == courseId }
            .let { mCourses.removeAt(it) }

        mCoursesObservable.offer(mCourses)
    }

    private fun CourseDraft.asCourse() = Course(
        id = CourseId(id.value),
        title = title,
        creationDate = Date(),
        items = items.map { item ->
            when (item) {
                is DraftItem.Quiz ->
                    CourseItem.Quiz(
                        id = QuizId(item.draft.id.value),
                        questions = item.draft.questions
                            .mapIndexed { pos, questionDraft ->
                                Question(
                                    id = QuestionId(questionDraft.id.value),
                                    text = questionDraft.text,
                                    variants = questionDraft.variants.map { variantDraft ->
                                        Variant(
                                            id = VariantId(variantDraft.id.value),
                                            text = variantDraft.text,
                                            isCorrect = variantDraft.isCorrect,
                                            isSelectedAsCorrect = true
                                        )
                                    },
                                    isMultipleMode = questionDraft.isMultipleMode,
                                    position = pos,
                                    result = QuestionResult.UNDEFINED
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

    private fun ImageSource.asPlayerImageSource() =
        ru.iandreyshev.player_core.image.ImageSource(
            when (this) {
                is ImageSource.Gallery -> filePath
                is ImageSource.Photo -> filePath
            }
        )

    private fun VideoSource.asPlayerVideoSource() =
        ru.iandreyshev.player_core.video.VideoSource(filePath)

}
