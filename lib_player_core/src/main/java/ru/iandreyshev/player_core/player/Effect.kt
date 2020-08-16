package ru.iandreyshev.player_core.player

import ru.iandreyshev.player_core.course.PlayerItem

internal sealed class Effect {

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

    object Finish : Effect()

    class Error(val error: String) : Effect()

}
