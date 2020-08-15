package ru.iandreyshev.light.infrastructure.courseList

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.light.domain.ICourseRepository
import ru.iandreyshev.light.domain.IDraftSerializer
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId

class InMemoryCourseRepository(
    private val storage: ICourseStorage
) : ICourseRepository, IDraftSerializer {

    private val mCourses = mutableListOf<Course>()
    private val mCoursesObservable = ConflatedBroadcastChannel<List<Course>>()

    override fun prepare() {
        mCourses.addAll(storage.list())
        mCoursesObservable.offer(mCourses)
    }

    override fun getCourse(id: CourseId): Course? {
        return mCourses.firstOrNull { it.id == id }
    }

    override fun getCoursesObservable(): Flow<List<Course>> =
        mCoursesObservable.asFlow()

    override fun save(draft: CourseDraft) {
        val course = storage.add(draft)
        mCourses.add(course)
        mCoursesObservable.offer(mCourses)
    }

    override fun delete(courseId: CourseId) {
        storage.remove(courseId)
        mCourses.indexOfFirst { it.id == courseId }
            .let { mCourses.removeAt(it) }

        mCoursesObservable.offer(mCourses)
    }

}
