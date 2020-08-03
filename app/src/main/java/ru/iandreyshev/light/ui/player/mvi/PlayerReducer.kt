package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.element.Reducer
import ru.iandreyshev.light.ui.player.PlayerItemState

class PlayerReducer : Reducer<State, Effect> {

    override fun invoke(state: State, effect: Effect): State =
        when (state.type) {
            State.Type.PREPARE_PLAYER -> when (effect) {
                is Effect.PlayImage -> state.toPlayingItemState()
                    .copy(
                        itemState = PlayerItemState.Image(effect.uri),
                        itemsCount = effect.itemsCount,
                        itemPosition = 0
                    )
                is Effect.PlayQuiz -> state.toPlayingItemState()
                    .copy(
                        itemState = PlayerItemState.Quiz,
                        itemsCount = effect.itemsCount,
                        itemPosition = 0
                    )
                is Effect.Error -> state.toPreparePlayerErrorState()
                    .copy(error = effect.error)
                else -> state
            }
            State.Type.PLAYING_ITEM -> when (effect) {
                is Effect.PlayImage -> state.toPlayingItemState()
                    .copy(
                        itemState = PlayerItemState.Image(effect.uri),
                        itemPosition = effect.itemPosition
                    )
                is Effect.PlayQuiz -> state.toPlayingItemState()
                    .copy(
                        itemState = PlayerItemState.Quiz,
                        itemPosition = effect.itemPosition
                    )
                is Effect.Finish -> state.toResultState()
                    .copy(result = effect.result)
                is Effect.Error -> state.toPlayingItemErrorState()
                    .copy(error = effect.error)
                else -> state
            }
            State.Type.RESULT -> state
            State.Type.PREPARE_PLAYER_ERROR -> when (effect) {
                is Effect.PreparePlayer -> state.toPreparePlayerState()
                else -> state
            }
            State.Type.PLAYING_ITEM_ERROR -> when (effect) {
                is Effect.PlayImage -> state.toPlayingItemState()
                    .copy(
                        itemState = PlayerItemState.Image(effect.uri),
                        itemPosition = effect.itemPosition
                    )
                is Effect.PlayQuiz -> state.toPlayingItemState()
                    .copy(
                        itemState = PlayerItemState.Quiz,
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
