package ru.iandreyshev.constructor.navigation

import androidx.navigation.NavController
import ru.iandreyshev.constructor.ui.editor.EditorFragmentDirections

class NavComponentRouter(
    private val controller: NavController
) : IRouter {

    override fun back() {
        controller.popBackStack()
    }

    override fun openQuizMaker() {
        EditorFragmentDirections
            .actionEditorFragmentToQuizMakerFragment()
            .let(controller::navigate)
    }

    override fun openImageMaker() {
        EditorFragmentDirections
            .actionEditorFragmentToImageMakerFragment()
            .let(controller::navigate)
    }

    override fun openVideoMaker() {
        EditorFragmentDirections
            .actionEditorFragmentToVideoMaker()
            .let(controller::navigate)
    }

}
