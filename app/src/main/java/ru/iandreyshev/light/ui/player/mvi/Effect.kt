package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.course.CourseItem

sealed class Effect {
    object PreparePlayer : Effect()
    class Start(val item: CourseItem, val itemsCount: Int) : Effect()
    class Play(val item: CourseItem, val itemPosition: Int) : Effect()
    class Finish(val result: String) : Effect()
    class Error(val error: String) : Effect()
}
