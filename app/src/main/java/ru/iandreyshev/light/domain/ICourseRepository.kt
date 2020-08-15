package ru.iandreyshev.light.domain

import kotlinx.coroutines.flow.Flow
import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId

interface ICourseRepository {
    fun prepare()
    fun getCourse(id: CourseId): Course?
    fun getCoursesObservable(): Flow<List<Course>>
    fun delete(courseId: CourseId)
}
