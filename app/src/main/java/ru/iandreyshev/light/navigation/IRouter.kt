package ru.iandreyshev.light.navigation

import ru.iandreyshev.light.ui.player.PlayerArgs

interface IRouter {
    fun openCourseEditor()
    fun back()
    fun openQuizMaker()
    fun openImageMaker()
    fun openVideoMaker()
    fun openPlayer(args: PlayerArgs)
}
