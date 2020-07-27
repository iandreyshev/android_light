package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.course.CourseItem
import ru.iandreyshev.light.ui.player.CoursePlaybackState

sealed class Effect {
    class StartPlaying(val item: CourseItem, val itemsCount: Int) : Effect()
    class PlayNext(val item: CourseItem) : Effect()
    class PlaybackUpdated(val playback: CoursePlaybackState) : Effect()
    class FinishPlaying(val result: String) : Effect()
    class Error(val error: String) : Effect()
}
