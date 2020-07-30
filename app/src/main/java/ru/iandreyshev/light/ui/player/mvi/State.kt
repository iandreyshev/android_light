package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.player.ItemState

data class State(
    val type: Type = Type.PREPARE_PLAYER,
    val itemsCount: Int = 0,
    val itemState: ItemState? = null,
    val itemPosition: Int = 0,
    val canForward: Boolean = false,
    val canBack: Boolean = false,
    val error: String = "",
    val result: String? = null
) {

    enum class Type {
        PREPARE_PLAYER,
        PREPARE_PLAYER_ERROR,
        PLAYING_ITEM,
        PLAYING_ITEM_ERROR,
        RESULT;
    }

}
