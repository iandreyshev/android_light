package ru.iandreyshev.light.ui.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.light.ui.player.mvi.*
import ru.iandreyshev.light.ui.player.quiz.mvi.PlayerNewsToQuizWish
import ru.iandreyshev.light.ui.player.quiz.mvi.QuizPlayerFeature
import ru.iandreyshev.light.ui.player.quiz.mvi.QuizPlayerState
import ru.iandreyshev.light.ui.player.quiz.mvi.QuizWish
import ru.iandreyshev.light.utill.*

class PlayerViewModel(
    scope: Scope,
    private val playerArgs: PlayerArgs
) : ViewModel() {

    val playerState by uiLazy { mPlayerState.distinctUntilChanged() }
    val quizPlayerState by uiLazy { mQuizPlayerState.distinctUntilChanged() }
    val eventShowNews = singleLiveEvent<News>()

    private val mPlayerState = MutableLiveData<State>()
    private val mQuizPlayerState = MutableLiveData<QuizPlayerState>()

    private val mPlayerFeature = PlayerFeature(
        coursePlayer = scope.get { parametersOf(playerArgs.courseId) }
    )

    private val mQuizPlayerFeature = QuizPlayerFeature(
        player = scope.get()
    )

    private val mBinders = mutableListOf<Binder>().apply {
        this += Binder().apply {
            bind(mPlayerFeature.news to mQuizPlayerFeature using PlayerNewsToQuizWish())
        }
        this += Binder().apply {
            bind(mQuizPlayerFeature.news to mPlayerFeature using QuizNewsToPlayerWish())
        }
    }

    private val mDisposables = CompositeDisposable().apply {
        this += Observable.wrap(mPlayerFeature).subscribe(mPlayerState::setValue)
        this += Observable.wrap(mPlayerFeature.news).subscribe(eventShowNews::invoke)

        this += Observable.wrap(mQuizPlayerFeature).subscribe(mQuizPlayerState::setValue)
    }

    fun onCreate() {
        mPlayerFeature.accept(Wish.Start)
    }

    operator fun invoke(uiAction: UiAction) {
        mPlayerFeature.accept(
            when (uiAction) {
                UiAction.Forward -> Wish.Forward
                UiAction.Back -> Wish.Back
                UiAction.Repeat -> Wish.Repeat
                UiAction.LoadImageError -> Wish.ShowError
            }
        )
    }

    operator fun invoke(wish: QuizWish) {
        mQuizPlayerFeature.accept(wish)
    }

    override fun onCleared() {
        super.onCleared()
        mPlayerFeature.dispose()
        mDisposables.dispose()
    }

}
