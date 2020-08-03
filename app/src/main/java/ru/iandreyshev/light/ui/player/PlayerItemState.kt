package ru.iandreyshev.light.ui.player

sealed class PlayerItemState {

    data class Image(val uri: String) : PlayerItemState()

    object Quiz : PlayerItemState()

}
