package ru.iandreyshev.player_core.course

sealed class PlayerItemState {

    data class Image(val uri: String) : PlayerItemState()

    object Quiz : PlayerItemState()

    data class Video(val uri: String) : PlayerItemState()

}
