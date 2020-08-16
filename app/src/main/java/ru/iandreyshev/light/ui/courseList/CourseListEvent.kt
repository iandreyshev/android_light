package ru.iandreyshev.light.ui.courseList

import ru.iandreyshev.light.ui.player.PlayerArgs

sealed class CourseListEvent {
    object OpenCourseEditor : CourseListEvent()
    class OpenPlayer(val args: PlayerArgs) : CourseListEvent()
    class ShowError(val text: String) : CourseListEvent()
}
