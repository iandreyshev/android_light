package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.iandreyshev.light.domain.player.*
import ru.iandreyshev.light.ui.player.PlayerItemState
import ru.iandreyshev.light.utill.just

class PlayerActor(
    private val player: ICoursePlayer
) : Actor<State, Wish, Effect> {

    override fun invoke(state: State, wish: Wish): Observable<out Effect> =
        when (state.type) {
            State.Type.PREPARE_PLAYER -> when (wish) {
                Wish.Start -> when (val result = player.prepare()) {
                    is PrepareResult.Success -> when (val item = result.item) {
                        is PlayerItem.Image ->
                            Effect.PlayImage(
                                uri = item.uri,
                                itemsCount = result.itemsCount,
                                itemPosition = 0
                            ).just()
                        is PlayerItem.Quiz ->
                            Effect.PlayQuiz(
                                quiz = item,
                                itemsCount = result.itemsCount,
                                itemPosition = 0
                            ).just()
                        is PlayerItem.Video -> TODO()
                    }
                    PrepareResult.ErrorGettingCourse,
                    PrepareResult.ErrorCourseIsEmpty ->
                        Effect.Error(result.toString()).just()
                }
                else -> noEffect()
            }
            State.Type.PREPARE_PLAYER_ERROR -> when (wish) {
                Wish.Repeat -> when (val result = player.prepare()) {
                    is PrepareResult.Success -> when (val item = result.item) {
                        is PlayerItem.Image ->
                            Effect.PlayImage(
                                uri = item.uri,
                                itemsCount = result.itemsCount,
                                itemPosition = 0
                            ).just()
                        is PlayerItem.Quiz ->
                            Effect.PlayQuiz(
                                quiz = item,
                                itemsCount = result.itemsCount,
                                itemPosition = 0
                            ).just()
                        is PlayerItem.Video -> TODO()
                    }
                    PrepareResult.ErrorGettingCourse,
                    PrepareResult.ErrorCourseIsEmpty ->
                        Effect.Error(result.toString()).just()
                }
                else -> noEffect()
            }
            State.Type.PLAYING_ITEM -> when (wish) {
                Wish.Forward -> when (val result = player.forward()) {
                    is MoveItemResult.Success -> when (val item = result.item) {
                        is PlayerItem.Image ->
                            Effect.PlayImage(
                                uri = item.uri,
                                itemPosition = result.itemPosition,
                                itemsCount = result.itemPosition
                            ).just()
                        is PlayerItem.Quiz ->
                            Effect.PlayQuiz(
                                quiz = item,
                                itemPosition = result.itemPosition,
                                itemsCount = result.itemPosition
                            ).just()
                        is PlayerItem.Video -> TODO()
                    }
                    MoveItemResult.MoveLimited ->
                        Effect.Finish("Results").just()
                }
                Wish.Back -> when (val result = player.back()) {
                    is MoveItemResult.Success -> when (val item = result.item) {
                        is PlayerItem.Image ->
                            Effect.PlayImage(
                                uri = item.uri,
                                itemPosition = result.itemPosition,
                                itemsCount = result.itemPosition
                            ).just()
                        is PlayerItem.Quiz ->
                            Effect.PlayQuiz(
                                quiz = item,
                                itemPosition = result.itemPosition,
                                itemsCount = result.itemPosition
                            ).just()
                        is PlayerItem.Video -> TODO()
                    }
                    else -> noEffect()
                }
                Wish.ShowError -> when (val item = state.itemState) {
                    is PlayerItemState.Image ->
                        Effect.Error("Error load image: ${item.uri}").just()
                    else -> noEffect()
                }
                else -> noEffect()
            }
            State.Type.PLAYING_ITEM_ERROR -> noEffect()
            State.Type.RESULT -> noEffect()
        }

    private fun noEffect() = Observable.empty<Effect>()

}
