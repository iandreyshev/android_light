package ru.iandreyshev.light.ui.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.core_utils.uiLazy
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.player_core.player.*
import ru.iandreyshev.player_core.quiz.QuizPlayerState
import ru.iandreyshev.player_core.quiz.QuizWish
import ru.iandreyshev.core_ui.singleLiveEvent
import ru.iandreyshev.player_core.Player
import ru.iandreyshev.player_core.video.VideoPlayerState

class PlayerViewModel(
    scope: Scope,
    private val playerArgs: PlayerArgs
) : ViewModel() {

    val playerState by uiLazy { mPlayerState.distinctUntilChanged() }
    val quizPlayerState by uiLazy { mQuizPlayerState.distinctUntilChanged() }
    val videoPlayerState by uiLazy { mVideoPlayerState.distinctUntilChanged() }

    val eventShowNews = singleLiveEvent<News>()

    private val mPlayerState = MutableLiveData<State>()
    private val mQuizPlayerState = MutableLiveData<QuizPlayerState>()
    private val mVideoPlayerState = MutableLiveData<VideoPlayerState>()

    private val mPlayer = Player(
        context = scope.androidContext(),
        dataSource = scope.get {
            parametersOf(playerArgs.courseId)
        }
    ).apply {
        onPlayerState(mPlayerState::setValue)
        onPlayerNews(eventShowNews::invoke)

        onQuizState(mQuizPlayerState::setValue)
    }

    fun onCreate() {
        mPlayer.accept(Wish.Start)
    }

    operator fun invoke(wish: PlayerWish) {
        mPlayer.accept(wish)
    }

    operator fun invoke(wish: QuizWish) {
        mPlayer.accept(wish)
    }

    override fun onCleared() {
        mPlayer.dispose()
    }

}
