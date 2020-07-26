package ru.iandreyshev.light.domain.courseList

import kotlinx.coroutines.flow.Flow

interface ICourseRepository {
    fun getCoursesObservable(): Flow<List<Course>>
    fun save(course: Course)
}
