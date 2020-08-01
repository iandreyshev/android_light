package ru.iandreyshev.light.domain.player

sealed class MoveItemResult {
    class Success(
        val item: PlayerCourseItem,
        val itemPosition: Int
    ) : MoveItemResult()

    object MoveLimited : MoveItemResult()
}
