package ru.iandreyshev.light.ui.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.player.quiz.IQuizPlayer
import ru.iandreyshev.light.ui.player.mvi.*
import ru.iandreyshev.light.ui.player.quiz.mvi.QuizPlayerFeature
import ru.iandreyshev.light.utill.*

class PlayerViewModel(
    scope: Scope,
    private val playerArgs: PlayerArgs
) : ViewModel(), QuizPlayerFeatureFactory {

    val state by uiLazy { mState.distinctUntilChanged() }
    val eventShowNews = singleLiveEvent<News>()

    private val mState = MutableLiveData<State>()

    private val mFeature = PlayerFeature(
        coursePlayer = scope.get { parametersOf(playerArgs.courseId) },
        quizPlayerFeatureFactory = this
    )
    private val mDisposables = CompositeDisposable().apply {
        this += Observable.wrap(mFeature).subscribe(mState::setValue)
        this += Observable.wrap(mFeature.news).subscribe(eventShowNews::invoke)
    }

    fun onCreate() {
        mFeature.accept(Wish.Start)
    }

    operator fun invoke(uiAction: UiAction) {
        mFeature.accept(
            when (uiAction) {
                UiAction.Forward -> Wish.Forward
                UiAction.Back -> Wish.Back
                UiAction.ApplyAnswer -> Wish.ApplyAnswer
                UiAction.Repeat -> Wish.Repeat
                UiAction.LoadImageError -> Wish.ShowError
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        mFeature.dispose()
        mDisposables.dispose()
    }

    override fun invoke(player: IQuizPlayer): QuizPlayerFeature {
        return QuizPlayerFeature(
            player = player
        ).also { quizPlayerFeature ->
            Observable.wrap(quizPlayerFeature.news)
                .map(QuizNewsToPlayerWish())
                .subscribe(mFeature)
        }
    }

}
