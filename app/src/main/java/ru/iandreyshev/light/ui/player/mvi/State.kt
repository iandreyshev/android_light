package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.course.CourseItem
import ru.iandreyshev.light.ui.player.CoursePlaybackState

data class State(
    val playbackItem: CourseItem? = null,
    val playback: CoursePlaybackState? = null,
    val playbackItemsCount: Int = 0,
    val result: String? = null
)
