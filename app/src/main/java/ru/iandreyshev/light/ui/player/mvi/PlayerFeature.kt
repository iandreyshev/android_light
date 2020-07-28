package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.feature.ActorReducerFeature
import ru.iandreyshev.light.domain.player.ICoursePlayer

class PlayerFeature(
    coursePlayer: ICoursePlayer
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    reducer = PlayerReducer(),
    actor = PlayerActor(coursePlayer),
    newsPublisher = NewsPublisher()
) {

    class NewsPublisher : Function3<Wish, Effect, State, News?> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.Error -> News.ToastNews(effect.error)
            else -> null
        }
    }

}
