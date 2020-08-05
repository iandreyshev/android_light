package ru.iandreyshev.player_core.player

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.iandreyshev.player_core.course.*
import ru.iandreyshev.player_core.utils.just

class PlayerActor(
    private val player: ICoursePlayer
) : Actor<State, Wish, Effect> {

    override fun invoke(state: State, wish: Wish): Observable<out Effect> =
        when (state.type) {
            State.Type.PREPARE_PLAYER -> when (wish) {
                Wish.Start -> when (val result = player.prepare()) {
                    is PrepareResult.Success -> when (val item = result.item) {
                        is PlayerItem.Image ->
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, 0),
                                Effect.PlayImage(item.uri)
                            )
                        is PlayerItem.Quiz ->
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, 0),
                                Effect.PlayQuiz(item)
                            )
                        is PlayerItem.Video ->
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, 0),
                                Effect.PlayVideo(item.uri)
                            )
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
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, 0),
                                Effect.PlayImage(item.uri)
                            )
                        is PlayerItem.Quiz ->
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, 0),
                                Effect.PlayQuiz(item)
                            )
                        is PlayerItem.Video ->
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, 0),
                                Effect.PlayVideo(item.uri)
                            )
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
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, result.position),
                                Effect.PlayImage(item.uri)
                            )
                        is PlayerItem.Quiz ->
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, result.position),
                                Effect.PlayQuiz(item)
                            )
                        is PlayerItem.Video ->
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, result.position),
                                Effect.PlayVideo(item.uri)
                            )
                    }
                    MoveItemResult.MoveLimited ->
                        Effect.Finish("Results").just()
                }
                Wish.Back -> when (val result = player.back()) {
                    is MoveItemResult.Success -> when (val item = result.item) {
                        is PlayerItem.Image ->
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, result.position),
                                Effect.PlayImage(item.uri)
                            )
                        is PlayerItem.Quiz ->
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, result.position),
                                Effect.PlayQuiz(item)
                            )
                        is PlayerItem.Video ->
                            Observable.just(
                                Effect.PlaybackStateChanged(result.itemsCount, result.position),
                                Effect.PlayVideo(item.uri)
                            )
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
