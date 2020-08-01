package ru.iandreyshev.light.ui.player.quiz.mvi

import com.badoo.mvicore.feature.ActorReducerFeature
import ru.iandreyshev.light.domain.player.quiz.IQuizPlayer

class QuizPlayerFeature(
    player: IQuizPlayer
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(
        questionsCount = player.questionsCount
    ),
    actor = QuizPlayerActor(player),
    reducer = QuizPlayerReducer(),
    newsPublisher = QuizPlayerNewsPublisher()
)
