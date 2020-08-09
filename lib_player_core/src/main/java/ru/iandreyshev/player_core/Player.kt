package ru.iandreyshev.player_core

import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import ru.iandreyshev.player_core.course.CoursePlayer
import ru.iandreyshev.player_core.course.IPlayerDataSource
import ru.iandreyshev.player_core.player.News
import ru.iandreyshev.player_core.player.PlayerFeature
import ru.iandreyshev.player_core.player.PlayerWish
import ru.iandreyshev.player_core.player.State
import ru.iandreyshev.player_core.quiz.QuizPlayer
import ru.iandreyshev.player_core.quiz.QuizPlayerFeature
import ru.iandreyshev.player_core.quiz.QuizPlayerState
import ru.iandreyshev.player_core.quiz.QuizWish

class Player(
    dataSource: IPlayerDataSource
) : IWishListener {

    private var mPlayerStateListener = BehaviorSubject.create<State>()
    private var mPlayerStateListenerDisposable = Disposables.empty()

    private var mPlayerNewsListener = BehaviorSubject.create<News>()
    private var mPlayerNewsListenerDisposable = Disposables.empty()

    private var mQuizStateListener = BehaviorSubject.create<QuizPlayerState>()
    private var mQuizStateListenerDisposable = Disposables.empty()

    fun onPlayerState(listener: (State) -> Unit) {
        mPlayerStateListenerDisposable.dispose()
        mPlayerStateListenerDisposable = mPlayerStateListener.subscribe(listener)
    }

    fun onPlayerNews(listener: (News) -> Unit) {
        mPlayerNewsListenerDisposable.dispose()
        mPlayerNewsListenerDisposable = mPlayerNewsListener.subscribe(listener)
    }

    fun onQuizState(listener: (QuizPlayerState) -> Unit) {
        mQuizStateListenerDisposable.dispose()
        mQuizStateListenerDisposable = mQuizStateListener.subscribe(listener)
    }

    override fun invoke(wish: PlayerWish) {
        mPlayerFeature.accept(wish)
    }

    override fun invoke(wish: QuizWish) {
        mQuizPlayerFeature.accept(wish)
    }

    private val mPlayer = CoursePlayer(
        dataSource = dataSource
    )

    private val mPlayerFeature = PlayerFeature(
        coursePlayer = mPlayer
    )

    private val mQuizPlayerFeature =
        QuizPlayerFeature(
            player = QuizPlayer()
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
        this += Observable.wrap(mPlayerFeature)
            .subscribe(mPlayerStateListener::onNext)
        this += Observable.wrap(mPlayerFeature.news)
            .subscribe(mPlayerNewsListener::onNext)

        this += Observable.wrap(mQuizPlayerFeature)
            .subscribe(mQuizStateListener::onNext)

        mBinders.forEach {
            this += it
        }

        this += mPlayerFeature
        this += mQuizPlayerFeature
    }

    fun dispose() {
        mDisposables.dispose()
        mQuizStateListenerDisposable.dispose()
        mPlayerNewsListenerDisposable.dispose()
        mPlayerStateListenerDisposable.dispose()
    }

}
