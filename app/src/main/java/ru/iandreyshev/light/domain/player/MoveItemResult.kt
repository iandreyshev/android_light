package ru.iandreyshev.light.domain.player

sealed class MoveItemResult {
    class Success(
        val item: ItemState,
        val itemPosition: Int
    ) : MoveItemResult()

    object MoveLimited : MoveItemResult()
}
