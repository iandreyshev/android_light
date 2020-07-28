package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.domain.course.CourseItem

sealed class PrepareResult {
    class Success(val item: CourseItem, val itemsCount: Int) : PrepareResult()
    object ErrorGettingCourse : PrepareResult()
    object ErrorCourseIsEmpty : PrepareResult()
}
