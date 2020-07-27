package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.feature.ActorReducerFeature
import ru.iandreyshev.light.domain.player.IPlayer

class PlayerFeature(
    player: IPlayer
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    reducer = PlayerReducer(),
    actor = PlayerActor(player),
    newsPublisher = NewsPublisher()
) {

    class NewsPublisher : Function3<Wish, Effect, State, News?> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.Error -> News.ToastNews(effect.error)
            else -> null
        }
    }

}
