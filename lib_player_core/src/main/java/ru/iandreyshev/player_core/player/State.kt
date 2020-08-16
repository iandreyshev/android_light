package ru.iandreyshev.player_core.player

import ru.iandreyshev.player_core.course.PlayerItemState

data class State(
    val type: Type = Type.PREPARE_PLAYER,
    val itemsCount: Int = 0,
    val itemState: PlayerItemState? = null,
    val itemPosition: Int = 0,
    val canForward: Boolean = false,
    val canBack: Boolean = false,
    val error: String = ""
) {

    enum class Type {
        PREPARE_PLAYER,
        PREPARE_PLAYER_ERROR,
        PLAYING_ITEM,
        PLAYING_ITEM_ERROR,
        RESULT;
    }

}
