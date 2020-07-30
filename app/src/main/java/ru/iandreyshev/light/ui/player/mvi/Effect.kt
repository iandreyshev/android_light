package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.player.ItemState

sealed class Effect {
    object PreparePlayer : Effect()
    class Start(val item: ItemState, val itemsCount: Int) : Effect()
    class Play(val item: ItemState, val itemPosition: Int) : Effect()
    class Finish(val result: String) : Effect()
    class Error(val error: String) : Effect()
}
