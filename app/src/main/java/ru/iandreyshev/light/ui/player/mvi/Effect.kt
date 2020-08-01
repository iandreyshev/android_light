package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.ui.player.CourseItemState

sealed class Effect {

    object PreparePlayer : Effect()

    class Start(
        val item: CourseItemState,
        val itemsCount: Int
    ) : Effect()

    class Play(
        val item: CourseItemState,
        val itemPosition: Int
    ) : Effect()

    class Finish(val result: String) : Effect()

    class Error(val error: String) : Effect()

}
