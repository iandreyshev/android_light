package ru.iandreyshev.light.ui.player

data class CoursePlaybackState(
    val itemNumber: Int,
    val itemProgress: Float
) {

    companion object {
        fun zero() = CoursePlaybackState(0, 0f)
    }

}
