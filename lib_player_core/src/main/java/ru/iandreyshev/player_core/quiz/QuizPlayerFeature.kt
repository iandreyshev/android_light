package ru.iandreyshev.player_core.quiz

import com.badoo.mvicore.feature.ActorReducerFeature

internal class QuizPlayerFeature(
    player: IQuizPlayer
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = QuizPlayerState(),
    actor = QuizPlayerActor(player),
    reducer = QuizPlayerReducer(),
    newsPublisher = QuizPlayerNewsPublisher()
)
