package ru.iandreyshev.light.domain.course

import kotlinx.coroutines.flow.Flow

interface ICourseRepository {
    fun getCourse(id: CourseId): Course?
    fun getCoursesObservable(): Flow<List<Course>>
    fun save(course: Course)
}
