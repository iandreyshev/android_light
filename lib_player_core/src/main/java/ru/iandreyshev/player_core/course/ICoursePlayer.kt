package ru.iandreyshev.player_core.course

interface ICoursePlayer {
    fun prepare(): PrepareResult
    fun forward(): MoveItemResult
    fun back(): MoveItemResult
}
