package ru.iandreyshev.player_core.player

import ru.iandreyshev.player_core.quiz.IQuizPlayer
import ru.iandreyshev.player_core.quiz.QuizPlayerFeature
import ru.iandreyshev.player_core.quiz.QuizPlayerState

typealias QuizPlayerFeatureFactory =
            (QuizPlayerState, IQuizPlayer) -> QuizPlayerFeature
