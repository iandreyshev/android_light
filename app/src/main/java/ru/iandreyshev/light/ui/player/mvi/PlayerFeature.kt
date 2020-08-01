package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.feature.ActorReducerFeature
import ru.iandreyshev.light.domain.player.ICoursePlayer

class PlayerFeature(
    coursePlayer: ICoursePlayer,
    quizPlayerFeatureFactory: QuizPlayerFeatureFactory
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    reducer = PlayerReducer(),
    actor = PlayerActor(
        quizPlayerFeatureFactory = quizPlayerFeatureFactory,
        player = coursePlayer
    ),
    newsPublisher = PlayerNewsPublisher()
)
