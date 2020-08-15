package ru.iandreyshev.light.infrastructure.courseList

import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId

interface ICourseStorage {
    fun add(draft: CourseDraft): Course
    fun list(): List<Course>
    fun remove(courseId: CourseId)
}
