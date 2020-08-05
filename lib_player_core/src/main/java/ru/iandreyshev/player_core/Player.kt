package ru.iandreyshev.player_core

import android.content.Context
import android.net.Uri
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import ru.iandreyshev.player_core.course.CoursePlayer
import ru.iandreyshev.player_core.course.IPlayerDataSource
import ru.iandreyshev.player_core.quiz.QuizPlayer
import ru.iandreyshev.player_core.player.News
import ru.iandreyshev.player_core.player.PlayerFeature
import ru.iandreyshev.player_core.player.State
import ru.iandreyshev.player_core.player.Wish
import ru.iandreyshev.player_core.quiz.QuizPlayerFeature
import ru.iandreyshev.player_core.quiz.QuizPlayerState
import ru.iandreyshev.player_core.quiz.QuizWish
import ru.iandreyshev.player_core.video.VideoPlayer

class Player(
    private val context: Context,
    dataSource: IPlayerDataSource
) {

    private var mPlayerStateListener: (State) -> Unit = {}
    private var mPlayerNewsListener: (News) -> Unit = {}
    private var mQuizStateListener: (QuizPlayerState) -> Unit = {}

    fun onPlayerState(listener: (State) -> Unit) {
        mPlayerStateListener = listener
    }

    fun onPlayerNews(listener: (News) -> Unit) {
        mPlayerNewsListener = listener
    }

    fun onQuizState(listener: (QuizPlayerState) -> Unit) {
        mQuizStateListener = listener
    }

    fun accept(wish: Wish) {
        mPlayerFeature.accept(wish)
    }

    fun accept(wish: QuizWish) {
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

    private val mVideoPlayer = VideoPlayer(
        playerProvider = { uri ->
            SimpleExoPlayer.Builder(context)
                .build()
                .apply {
                    val dataSourceFactory = DefaultDataSourceFactory(
                        context,
                        Util.getUserAgent(context, "Light")
                    )
                    val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(uri.uri))

                    prepare(videoSource)
                }
        }
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
        this += Observable.wrap(mPlayerFeature).subscribe { mPlayerStateListener(it) }
        this += Observable.wrap(mPlayerFeature.news).subscribe { mPlayerNewsListener(it) }

        this += Observable.wrap(mQuizPlayerFeature).subscribe { mQuizStateListener(it) }

        mBinders.forEach {
            this += it
        }

        this += mPlayerFeature
        this += mQuizPlayerFeature
    }

    fun dispose() {
        mVideoPlayer.dispose()
        mDisposables.dispose()
    }

}
