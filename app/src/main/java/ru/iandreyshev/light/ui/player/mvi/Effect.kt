package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.player.PlayerItem

sealed class Effect {

    object PreparePlayer : Effect()

    class PlayImage(
        val uri: String,
        val itemPosition: Int,
        val itemsCount: Int
    ) : Effect()

    class PlayQuiz(
        val quiz: PlayerItem.Quiz,
        val itemPosition: Int,
        val itemsCount: Int
    ) : Effect()

    class Finish(val result: String) : Effect()

    class Error(val error: String) : Effect()

}
