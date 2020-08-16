package ru.iandreyshev.player_core.player

import com.badoo.mvicore.element.Reducer
import ru.iandreyshev.player_core.course.PlayerItemState

internal class PlayerReducer : Reducer<State, Effect> {

    override fun invoke(state: State, effect: Effect): State =
        when (state.type) {
            State.Type.PREPARE_PLAYER -> when (effect) {
                is Effect.PlayImage ->
                    state.toPlayingItemState()
                        .copy(itemState = PlayerItemState.Image(effect.uri))
                is Effect.PlayQuiz ->
                    state.toPlayingItemState()
                        .copy(itemState = PlayerItemState.Quiz)
                is Effect.PlayVideo ->
                    state.toPlayingItemState()
                        .copy(itemState = PlayerItemState.Video(effect.uri))
                is Effect.Error ->
                    state.toPreparePlayerErrorState()
                        .copy(error = effect.error)
                is Effect.PlaybackStateChanged ->
                    state.copy(
                        itemsCount = effect.itemsCount,
                        itemPosition = effect.itemPosition
                    )
                else -> state
            }
            State.Type.PLAYING_ITEM -> when (effect) {
                is Effect.PlayImage ->
                    state.toPlayingItemState()
                        .copy(itemState = PlayerItemState.Image(effect.uri))
                is Effect.PlayQuiz ->
                    state.toPlayingItemState()
                        .copy(itemState = PlayerItemState.Quiz)
                is Effect.PlayVideo ->
                    state.toPlayingItemState()
                        .copy(itemState = PlayerItemState.Video(effect.uri))
                is Effect.Finish ->
                    state.toResultState()
                        .copy(itemState = null)
                is Effect.PlaybackStateChanged ->
                    state.copy(
                        itemsCount = effect.itemsCount,
                        itemPosition = effect.itemPosition
                    )
                is Effect.Error ->
                    state.toPlayingItemErrorState()
                        .copy(
                            error = effect.error,
                            itemState = null
                        )
                else -> state
            }
            State.Type.RESULT -> state
            State.Type.PREPARE_PLAYER_ERROR -> when (effect) {
                is Effect.PreparePlayer -> state.toPreparePlayerState()
                else -> state
            }
            State.Type.PLAYING_ITEM_ERROR -> when (effect) {
                is Effect.PlayImage ->
                    state.toPlayingItemState()
                        .copy(itemState = PlayerItemState.Image(effect.uri))
                is Effect.PlayQuiz ->
                    state.toPlayingItemState()
                        .copy(itemState = PlayerItemState.Quiz)
                is Effect.PlayVideo ->
                    state.toPlayingItemState()
                        .copy(itemState = PlayerItemState.Video(effect.uri))
                is Effect.PlaybackStateChanged ->
                    state.copy(
                        itemsCount = effect.itemsCount,
                        itemPosition = effect.itemPosition
                    )
                else -> state
            }
        }

    private fun State.toPreparePlayerState() =
        copy(type = State.Type.PREPARE_PLAYER)

    private fun State.toPreparePlayerErrorState() =
        copy(type = State.Type.PREPARE_PLAYER_ERROR)

    private fun State.toPlayingItemState() =
        copy(type = State.Type.PLAYING_ITEM)

    private fun State.toPlayingItemErrorState() =
        copy(type = State.Type.PLAYING_ITEM_ERROR)

    private fun State.toResultState() =
        copy(type = State.Type.RESULT)

}
