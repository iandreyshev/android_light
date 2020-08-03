package ru.iandreyshev.light.ui.player

sealed class UiAction {
    object LoadImageError : UiAction()
    object Forward : UiAction()
    object Back : UiAction()
    object Repeat : UiAction()
}
