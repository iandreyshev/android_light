package ru.iandreyshev.player

import ru.iandreyshev.player_core.player.Wish

internal sealed class UiAction {
    object LoadImageError : UiAction()
    object Forward : UiAction()
    object Back : UiAction()
    object Repeat : UiAction()
}

internal fun UiAction.asWish() = when (this) {
    UiAction.Forward -> Wish.Forward
    UiAction.Back -> Wish.Back
    UiAction.Repeat -> Wish.Repeat
    UiAction.LoadImageError -> Wish.ShowError
}
