package ru.iandreyshev.light.domain

import kotlinx.coroutines.flow.Flow
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId

interface ICourseRepository {
    fun getCourse(id: CourseId): Course?
    fun getCoursesObservable(): Flow<List<Course>>
    fun save(course: Course)
}
