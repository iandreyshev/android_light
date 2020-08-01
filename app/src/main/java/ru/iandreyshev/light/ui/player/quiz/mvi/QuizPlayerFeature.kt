package ru.iandreyshev.light.ui.player.quiz.mvi

import com.badoo.mvicore.feature.ActorReducerFeature
import ru.iandreyshev.light.domain.player.quiz.IQuizPlayer

class QuizPlayerFeature(
    player: IQuizPlayer
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(
        questionText = player.currentQuestion.text,
        questionIndex = player.currentQuestionPosition,
        questionsCount = player.questionsCount,
        variants = player.currentQuestion.variants,
        isMultipleMode = player.currentQuestion.isMultipleMode,
        questionResult = player.currentQuestion.result
    ),
    actor = QuizPlayerActor(player),
    reducer = QuizPlayerReducer(),
    newsPublisher = QuizPlayerNewsPublisher()
)
