package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.feature.ActorReducerFeature
import ru.iandreyshev.light.domain.player.ICoursePlayer

class PlayerFeature(
    coursePlayer: ICoursePlayer
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    reducer = PlayerReducer(),
    actor = PlayerActor(
        player = coursePlayer
    ),
    newsPublisher = PlayerNewsPublisher()
)
