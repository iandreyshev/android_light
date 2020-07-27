package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.iandreyshev.light.domain.player.IPlayer
import ru.iandreyshev.light.domain.player.LoadResult

class PlayerActor(
    private val player: IPlayer
) : Actor<State, Wish, Effect> {

    override fun invoke(state: State, wish: Wish): Observable<out Effect> =
        when (wish) {
            Wish.Start -> {
                player.prepare()
                    .map { loadResult ->
                        when (loadResult) {
                            is LoadResult.Success ->
                                Effect.StartPlaying(loadResult.item, loadResult.itemsCount)
                            LoadResult.UnknownError ->
                                Effect.Error("Load error")
                        }
                    }
            }
            else -> Observable.empty()
        }

    private fun just(effect: Effect) = Observable.just(effect)

}
