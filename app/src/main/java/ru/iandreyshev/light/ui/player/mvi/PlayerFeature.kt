package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.feature.ActorReducerFeature
import ru.iandreyshev.light.domain.player.ICoursePlayer
import ru.iandreyshev.light.ui.player.quiz.mvi.QuizPlayerFeature

class PlayerFeature(
    coursePlayer: ICoursePlayer,
    quizPlayerFeatureFactory: QuizPlayerFeatureFactory
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    reducer = PlayerReducer(),
    actor = PlayerActor(
        quizPlayerFeatureFactory = { quizPlayer ->
            QuizPlayerFeature(quizPlayer)
        },
        player = coursePlayer
    ),
    newsPublisher = PlayerNewsPublisher()
)
