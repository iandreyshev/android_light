package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.element.Reducer
import ru.iandreyshev.light.ui.player.CoursePlaybackState

class PlayerReducer : Reducer<State, Effect> {

    override fun invoke(state: State, effect: Effect): State =
        when (effect) {
            is Effect.StartPlaying ->
                state.copy(
                    playbackItem = effect.item,
                    playback = CoursePlaybackState.zero(),
                    playbackItemsCount = effect.itemsCount
                )
            is Effect.PlayNext ->
                state.copy(
                    playbackItem = effect.item,
                    playback = CoursePlaybackState.zero()
                )
            is Effect.PlaybackUpdated ->
                state.copy(
                    playback = effect.playback
                )
            is Effect.FinishPlaying ->
                state.copy(
                    result = effect.result
                )
            is Effect.Error -> state
        }

}
