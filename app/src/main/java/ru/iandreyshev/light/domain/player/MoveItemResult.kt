package ru.iandreyshev.light.domain.player

sealed class MoveItemResult {
    class Success(
        val item: PlayerItem,
        val itemPosition: Int,
        val itemsCount: Int
    ) : MoveItemResult()

    object MoveLimited : MoveItemResult()
}
