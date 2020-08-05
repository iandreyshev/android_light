package ru.iandreyshev.player_core.player

import com.badoo.mvicore.feature.ActorReducerFeature
import ru.iandreyshev.player_core.course.ICoursePlayer

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
