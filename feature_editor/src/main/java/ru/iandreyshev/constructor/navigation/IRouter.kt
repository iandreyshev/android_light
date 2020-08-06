package ru.iandreyshev.constructor.navigation

import ru.iandreyshev.constructor.ui.imageMaker.ImageMakerArgs
import ru.iandreyshev.constructor.ui.quizMaker.QuizMakerArgs
import ru.iandreyshev.constructor.ui.videoMaker.VideoMakerArgs

internal interface IRouter {
    fun back()
    fun openQuizMaker(args: QuizMakerArgs)
    fun openImageMaker(args: ImageMakerArgs)
    fun openVideoMaker(args: VideoMakerArgs)
}
