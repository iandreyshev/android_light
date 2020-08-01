package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.iandreyshev.light.domain.player.*
import ru.iandreyshev.light.ui.player.CourseItemState

class PlayerActor(
    private val quizPlayerFeatureFactory: QuizPlayerFeatureFactory,
    private val player: ICoursePlayer
) : Actor<State, Wish, Effect> {

    override fun invoke(state: State, wish: Wish): Observable<out Effect> =
        when (state.type) {
            State.Type.PREPARE_PLAYER -> when (wish) {
                Wish.Start -> when (val result = player.prepare()) {
                    is PrepareResult.Success -> {
                        Effect.Start(
                            courseItemState(state, result.item),
                            result.itemsCount
                        ).just()
                    }
                    PrepareResult.ErrorGettingCourse,
                    PrepareResult.ErrorCourseIsEmpty ->
                        Effect.Error(result.toString()).just()
                }
                else -> noEffect()
            }
            State.Type.PREPARE_PLAYER_ERROR -> when (wish) {
                Wish.Repeat -> when (val result = player.prepare()) {
                    is PrepareResult.Success -> {
                        Effect.Start(
                            courseItemState(state, result.item),
                            result.itemsCount
                        ).just()
                    }
                    PrepareResult.ErrorGettingCourse,
                    PrepareResult.ErrorCourseIsEmpty ->
                        Effect.Error(result.toString()).just()
                }
                else -> noEffect()
            }
            State.Type.PLAYING_ITEM -> when (wish) {
                Wish.Forward -> when (val result = player.forward()) {
                    is MoveItemResult.Success ->
                        Effect.Play(
                            courseItemState(state, result.item),
                            result.itemPosition
                        ).just()
                    MoveItemResult.MoveLimited ->
                        Effect.Finish("Results").just()
                }
                Wish.Back -> when (val result = player.back()) {
                    is MoveItemResult.Success ->
                        Effect.Play(
                            courseItemState(state, result.item),
                            result.itemPosition
                        ).just()
                    MoveItemResult.MoveLimited ->
                        noEffect()
                }
                Wish.ShowError -> when (val item = state.itemState) {
                    is CourseItemState.Image ->
                        Effect.Error("Error load image: ${item.uri}").just()
                    else -> noEffect()
                }
                Wish.ApplyAnswer -> noEffect()
                else -> noEffect()
            }
            State.Type.PLAYING_ITEM_ERROR -> noEffect()
            State.Type.RESULT -> noEffect()
        }

    private fun courseItemState(
        state: State,
        item: PlayerCourseItem
    ): CourseItemState {
        state.itemState?.dispose()

        return when (item) {
            is PlayerCourseItem.Image -> CourseItemState.Image(item.uri)
            is PlayerCourseItem.Quiz -> {
                state.itemState?.dispose()
                CourseItemState.Quiz(
                    quizPlayerFeatureFactory(item.player)
                )
            }
            is PlayerCourseItem.Video -> TODO()
        }
    }

    private fun just(effect: Effect) = Observable.just(effect)
    private fun <T> T.just() = Observable.just(this)
    private fun noEffect() = Observable.empty<Effect>()

}
