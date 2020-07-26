package ru.iandreyshev.light.ui.player.mvi

import com.badoo.mvicore.feature.ActorReducerFeature

class PlayerFeature : ActorReducerFeature<Wish, Effect, State, Nothing>(
    initialState = State.Preloading,
    reducer = PlayerReducer(),
    actor = PlayerActor()
)
