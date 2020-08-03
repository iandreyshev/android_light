package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.player.quiz.IQuizPlayer
import ru.iandreyshev.light.ui.player.quiz.mvi.QuizPlayerFeature
import ru.iandreyshev.light.ui.player.quiz.mvi.QuizPlayerState

typealias QuizPlayerFeatureFactory =
            (QuizPlayerState, IQuizPlayer) -> QuizPlayerFeature
