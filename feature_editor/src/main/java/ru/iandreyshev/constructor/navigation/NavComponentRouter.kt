package ru.iandreyshev.constructor.navigation

import androidx.navigation.NavController
import ru.iandreyshev.constructor.ui.editor.EditorFragmentDirections
import ru.iandreyshev.constructor.ui.imageMaker.ImageMakerArgs
import ru.iandreyshev.constructor.ui.quizMaker.QuizMakerArgs
import ru.iandreyshev.constructor.ui.videoMaker.VideoMakerArgs

class NavComponentRouter(
    private val controller: NavController
) : IRouter {

    override fun back() {
        controller.popBackStack()
    }

    override fun openQuizMaker(args: QuizMakerArgs) {
        EditorFragmentDirections
            .actionEditorFragmentToQuizMakerFragment(args)
            .let(controller::navigate)
    }

    override fun openImageMaker(args: ImageMakerArgs) {
        EditorFragmentDirections
            .actionEditorFragmentToImageMakerFragment(args)
            .let(controller::navigate)
    }

    override fun openVideoMaker(args: VideoMakerArgs) {
        EditorFragmentDirections
            .actionEditorFragmentToVideoMaker(args)
            .let(controller::navigate)
    }

}
