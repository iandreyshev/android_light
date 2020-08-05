package ru.iandreyshev.light.navigation

import ru.iandreyshev.light.ui.player.PlayerArgs

interface IRouter {
    fun openCourseEditor()
    fun back()
    fun openPlayer(args: PlayerArgs)
}
