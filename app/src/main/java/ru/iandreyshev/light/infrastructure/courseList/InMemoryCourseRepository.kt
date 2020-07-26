package ru.iandreyshev.light.infrastructure.courseList

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import ru.iandreyshev.light.domain.course.Course
import ru.iandreyshev.light.domain.course.ICourseRepository

class InMemoryCourseRepository : ICourseRepository {

    private val mCourses = mutableListOf<Course>()
    private val mCoursesObservable = ConflatedBroadcastChannel<List<Course>>()

    override fun getCoursesObservable(): Flow<List<Course>> =
        mCoursesObservable.asFlow()

    override fun save(course: Course) {
        mCourses.add(course)
        mCoursesObservable.offer(mCourses)
    }

}
