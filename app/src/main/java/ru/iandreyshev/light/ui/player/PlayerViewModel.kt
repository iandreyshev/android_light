package ru.iandreyshev.light.ui.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.light.ui.player.mvi.News
import ru.iandreyshev.light.ui.player.mvi.Wish
import ru.iandreyshev.light.ui.player.mvi.PlayerFeature
import ru.iandreyshev.light.ui.player.mvi.State
import ru.iandreyshev.light.utill.distinctUntilChanged
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.singleLiveEvent
import ru.iandreyshev.light.utill.uiLazy

class PlayerViewModel(
    scope: Scope,
    private val playerArgs: PlayerArgs
) : ViewModel() {

    val state by uiLazy { mState.distinctUntilChanged() }
    val eventShowNews = singleLiveEvent<News>()

    private val mState = MutableLiveData<State>()

    private val mFeature = PlayerFeature(scope.get { parametersOf(playerArgs.courseId) })
    private val mDisposables = CompositeDisposable().apply {
        this += Observable.wrap(mFeature).subscribe(mState::setValue)
        this += Observable.wrap(mFeature.news).subscribe(eventShowNews::invoke)
    }

    fun onCreate() {
        mFeature.accept(Wish.Start)
    }

    fun onAction(uiAction: UiAction) {
        mFeature.accept(
            when (uiAction) {
                UiAction.ForwardClick -> Wish.Next
                UiAction.BackClick -> Wish.Previous
                UiAction.ApplyAnswer -> Wish.ApplyAnswer
                UiAction.Exit -> Wish.Exit
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        mFeature.dispose()
        mDisposables.dispose()
    }

}