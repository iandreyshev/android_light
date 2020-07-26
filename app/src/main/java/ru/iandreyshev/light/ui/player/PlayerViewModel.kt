package ru.iandreyshev.light.ui.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import ru.iandreyshev.light.ui.player.mvi.Wish
import ru.iandreyshev.light.ui.player.mvi.PlayerFeature
import ru.iandreyshev.light.ui.player.mvi.State
import ru.iandreyshev.light.utill.distinctUntilChanged
import ru.iandreyshev.light.utill.uiLazy

class PlayerViewModel : ViewModel() {

    val state by uiLazy { mState.distinctUntilChanged() }

    private val mState = MutableLiveData<State>()

    private val mFeature = PlayerFeature()
    private val mFeatureDisposable = Observable.wrap(mFeature)
        .subscribe(mState::setValue)

    fun onPause() {
        mFeature.accept(Wish.Pause)
    }

    override fun onCleared() {
        super.onCleared()
        mFeature.dispose()
        mFeatureDisposable.dispose()
    }

}