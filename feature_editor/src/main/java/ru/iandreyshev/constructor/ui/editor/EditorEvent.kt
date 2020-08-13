package ru.iandreyshev.constructor.ui.editor

import ru.iandreyshev.constructor.ui.imageMaker.ImageMakerArgs
import ru.iandreyshev.constructor.ui.quizMaker.QuizMakerArgs
import ru.iandreyshev.constructor.ui.videoMaker.VideoMakerArgs

sealed class EditorEvent {
    class OpenQuizMaker(val args: QuizMakerArgs) : EditorEvent()
    class OpenImageMaker(val args: ImageMakerArgs) : EditorEvent()
    class OpenVideoMaker(val args: VideoMakerArgs) : EditorEvent()
    object Exit : EditorEvent()
}
