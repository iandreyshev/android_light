package ru.iandreyshev.player_core.course

internal interface ICoursePlayer {
    fun prepare(): PrepareResult
    fun forward(): MoveItemResult
    fun back(): MoveItemResult
}
