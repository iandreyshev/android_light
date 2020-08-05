package ru.iandreyshev.light.infrastructure.courseList

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId
import ru.iandreyshev.player_core.course.ICourseRepository

class InMemoryCourseRepository : ICourseRepository {

    private val mCourses = mutableListOf<Course>()
    private val mCoursesObservable = ConflatedBroadcastChannel<List<Course>>()

    override fun getCourse(id: CourseId): Course? {
        return mCourses.firstOrNull { it.id == id }
    }

    override fun getCoursesObservable(): Flow<List<Course>> =
        mCoursesObservable.asFlow()

    override fun save(course: Course) {
        mCourses.add(course)
        mCoursesObservable.offer(mCourses)
    }

}
