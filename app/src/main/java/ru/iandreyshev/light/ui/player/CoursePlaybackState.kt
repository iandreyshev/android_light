package ru.iandreyshev.light.ui.player

data class CoursePlaybackState(
    val partsCount: Int,
    val part: Int,
    val partProgress: Float
)
