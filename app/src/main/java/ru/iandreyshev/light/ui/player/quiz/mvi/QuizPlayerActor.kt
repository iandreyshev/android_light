package ru.iandreyshev.light.ui.player.quiz.mvi

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.iandreyshev.light.domain.player.quiz.IQuizPlayer

class QuizPlayerActor(
    private val player: IQuizPlayer
) : Actor<State, Wish, Effect> {

    override fun invoke(
        state: State,
        action: Wish
    ): Observable<out Effect> {
        TODO("Not yet implemented")
    }

}