package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable

class PlayerActor : Actor<State, Wish, Effect> {

    override fun invoke(state: State, wish: Wish): Observable<out Effect> {
        TODO("Not yet implemented")
    }

}
