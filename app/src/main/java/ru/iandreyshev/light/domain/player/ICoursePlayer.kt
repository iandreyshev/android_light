package ru.iandreyshev.light.domain.player

interface ICoursePlayer {
    fun prepare(): PrepareResult
    fun forward(): MoveItemResult
    fun back(): MoveItemResult
}
