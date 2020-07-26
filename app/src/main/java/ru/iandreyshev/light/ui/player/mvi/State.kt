package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.imageMaker.Image
import ru.iandreyshev.light.domain.quizMaker.QuizId
import ru.iandreyshev.light.domain.videoMaker.Video
import ru.iandreyshev.light.ui.player.CoursePlaybackState

sealed class State {
    object Preloading : State()

    data class PlayingQuiz(
        val id: QuizId,
        val coursePlayback: CoursePlaybackState
    ) : State()

    data class PlayingImage(
        val image: Image,
        val coursePlayback: CoursePlaybackState
    ) : State()

}
