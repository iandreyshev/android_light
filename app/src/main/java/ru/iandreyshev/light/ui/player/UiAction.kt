package ru.iandreyshev.light.ui.player

sealed class UiAction {
    object ForwardClick : UiAction()
    object BackClick : UiAction()
    object ApplyAnswer : UiAction()
    object Exit : UiAction()
}
