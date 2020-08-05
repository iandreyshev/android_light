package ru.iandreyshev.player_core.course

sealed class MoveItemResult {
    class Success(
        val item: PlayerItem,
        val itemsCount: Int,
        val position: Int,
    ) : MoveItemResult()

    object MoveLimited : MoveItemResult()
}
