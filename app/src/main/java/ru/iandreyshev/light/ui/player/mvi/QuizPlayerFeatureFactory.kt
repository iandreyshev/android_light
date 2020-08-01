package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.player.quiz.IQuizPlayer
import ru.iandreyshev.light.ui.player.quiz.mvi.QuizPlayerFeature

typealias QuizPlayerFeatureFactory =
            (IQuizPlayer) -> QuizPlayerFeature
