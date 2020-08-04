package ru.iandreyshev.light.ui.player.mvi

import ru.iandreyshev.light.domain.player.PlayerItem

sealed class Effect {

    object PreparePlayer : Effect()

    class PlaybackStateChanged(
        val itemsCount: Int,
        val itemPosition: Int
    ) : Effect()

    class PlayImage(
        val uri: String
    ) : Effect()

    class PlayQuiz(
        val quiz: PlayerItem.Quiz
    ) : Effect()

    class PlayVideo(
        val uri: String
    ) : Effect()

    class Finish(val result: String) : Effect()

    class Error(val error: String) : Effect()

}
