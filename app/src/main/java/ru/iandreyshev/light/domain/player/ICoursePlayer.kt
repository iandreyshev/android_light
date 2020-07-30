package ru.iandreyshev.light.domain.player

interface ICoursePlayer {
    fun playerForQuizAt(position: Int): IQuizPlayer
    fun prepare(): PrepareResult
    fun forward(): MoveItemResult
    fun back(): MoveItemResult
}
