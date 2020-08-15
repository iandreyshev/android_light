package ru.iandreyshev.player_core.course

internal sealed class MoveItemResult {
    class Success(
        val item: PlayerItem,
        val itemsCount: Int,
        val position: Int
    ) : MoveItemResult()

    object MoveLimited : MoveItemResult()
}
