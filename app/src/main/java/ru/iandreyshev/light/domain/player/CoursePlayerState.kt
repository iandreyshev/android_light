package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.ui.player.CourseItemState

sealed class CoursePlayerState {
    class Play(
        val item: CourseItemState,
        val itemsCount: Int
    ) : CoursePlayerState()

    object ErrorGettingCourse : CoursePlayerState()
    object ErrorCourseIsEmpty : CoursePlayerState()
}
