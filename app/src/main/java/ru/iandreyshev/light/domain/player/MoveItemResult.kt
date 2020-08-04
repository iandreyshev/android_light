package ru.iandreyshev.light.domain.player

sealed class MoveItemResult {
    class Success(
        val item: PlayerItem,
        val itemsCount: Int,
        val position: Int,
    ) : MoveItemResult()

    object MoveLimited : MoveItemResult()
}
