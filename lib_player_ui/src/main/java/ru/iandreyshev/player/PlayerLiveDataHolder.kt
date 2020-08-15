package ru.iandreyshev.player

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.core_ui.singleLiveEvent
import ru.iandreyshev.core_utils.uiLazy
import ru.iandreyshev.player_core.Player
import ru.iandreyshev.player_core.player.News
import ru.iandreyshev.player_core.player.State
import ru.iandreyshev.player_core.quiz.QuizPlayerState

class PlayerLiveDataHolder(player: Player) {

    val playerState by uiLazy { mPlayerState.distinctUntilChanged() }
    val quizPlayerState by uiLazy { mQuizPlayerState.distinctUntilChanged() }

    val eventShowNews = singleLiveEvent<News>()

    private val mPlayerState = MutableLiveData<State>()
    private val mQuizPlayerState = MutableLiveData<QuizPlayerState>()

    init {
        player.apply {
            onPlayerState(mPlayerState::setValue)
            onPlayerNews(eventShowNews::invoke)

            onQuizState(mQuizPlayerState::setValue)
        }
    }

    fun subscribe(lifecycleOwner: LifecycleOwner, view: PlayerView) {
        playerState.observe(lifecycleOwner, Observer(view::render))
        quizPlayerState.observe(lifecycleOwner, Observer(view::render))
    }

}
