package ru.iandreyshev.light.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import ru.iandreyshev.light.ui.courseList.CourseListFragmentDirections
import ru.iandreyshev.light.ui.editor.EditorFragmentDirections
import ru.iandreyshev.light.ui.player.PlayerArgs

class NavComponentRouter(
    private val controller: NavController
) : IRouter {

    override fun openCourseEditor() {
        CourseListFragmentDirections
            .actionCourseListFragmentToNavEditor()
            .let(controller::navigate)
    }

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

    override fun openPlayer(args: PlayerArgs) {
        CourseListFragmentDirections
            .actionCourseListFragmentToNavPlayer(args)
            .let(controller::navigate)
    }

}

fun Fragment.router() = NavComponentRouter(findNavController())
