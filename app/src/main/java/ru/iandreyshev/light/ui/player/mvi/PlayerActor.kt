package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.iandreyshev.light.domain.player.ICoursePlayer
import ru.iandreyshev.light.domain.player.ItemState
import ru.iandreyshev.light.domain.player.PrepareResult
import ru.iandreyshev.light.domain.player.MoveItemResult

class PlayerActor(
    private val coursePlayer: ICoursePlayer
) : Actor<State, Wish, Effect> {

    override fun invoke(state: State, wish: Wish): Observable<out Effect> =
        when (state.type) {
            State.Type.PREPARE_PLAYER -> when (wish) {
                Wish.Start -> when (val result = coursePlayer.prepare()) {
                    is PrepareResult.Success ->
                        Effect.Start(result.item, result.itemsCount).just()
                    PrepareResult.ErrorGettingCourse,
                    PrepareResult.ErrorCourseIsEmpty ->
                        Effect.Error(result.toString()).just()
                }
                else -> noEffect()
            }
            State.Type.PREPARE_PLAYER_ERROR -> when (wish) {
                Wish.Repeat -> when (val result = coursePlayer.prepare()) {
                    is PrepareResult.Success ->
                        Effect.Start(result.item, result.itemsCount).just()
                    PrepareResult.ErrorGettingCourse,
                    PrepareResult.ErrorCourseIsEmpty ->
                        Effect.Error(result.toString()).just()
                }
                else -> noEffect()
            }
            State.Type.PLAYING_ITEM -> when (wish) {
                Wish.Forward -> when (val result = coursePlayer.forward()) {
                    is MoveItemResult.Success ->
                        Effect.Play(result.item, result.itemPosition).just()
                    MoveItemResult.MoveLimited ->
                        Effect.Finish("Results").just()
                }
                Wish.Back -> when (val result = coursePlayer.back()) {
                    is MoveItemResult.Success ->
                        Effect.Play(result.item, result.itemPosition).just()
                    MoveItemResult.MoveLimited ->
                        noEffect()
                }
                Wish.ShowError -> when (val item = state.itemState) {
                    is ItemState.Image ->
                        Effect.Error("Error load image: ${item.uri}").just()
                    else -> noEffect()
                }
                Wish.ApplyAnswer -> noEffect()
                else -> noEffect()
            }
            State.Type.PLAYING_ITEM_ERROR -> noEffect()
            State.Type.RESULT -> noEffect()
        }

    private fun just(effect: Effect) = Observable.just(effect)
    private fun <T> T.just() = Observable.just(this)
    private fun noEffect() = Observable.empty<Effect>()

}
