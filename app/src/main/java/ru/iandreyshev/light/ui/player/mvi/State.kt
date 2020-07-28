package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.course.CourseItem

data class State(
    val type: Type = Type.PREPARE_PLAYER,
    val isPlayerPrepared: Boolean = false,
    val playerPrepareError: String? = null,
    val courseItemsCount: Int = 0,
    val courseItem: CourseItem? = null,
    val courseItemPosition: Int = 0,
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
