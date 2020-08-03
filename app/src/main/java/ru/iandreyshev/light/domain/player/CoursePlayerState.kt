package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.ui.player.PlayerItemState

sealed class CoursePlayerState {
    class Play(
        val item: PlayerItemState,
        val itemsCount: Int
    ) : CoursePlayerState()

    object ErrorGettingCourse : CoursePlayerState()
    object ErrorCourseIsEmpty : CoursePlayerState()
}
