package ru.iandreyshev.light.ui.player.video

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.koin.android.ext.koin.androidApplication
import org.koin.core.scope.Scope
import ru.iandreyshev.light.ui.player.PlayerItemState
import ru.iandreyshev.light.utill.distinctUntilChanged
import ru.iandreyshev.light.utill.uiLazy

class VideoPlayerViewModel(
    private val scope: Scope
) : ViewModel() {

    val player by uiLazy { mPlayer.distinctUntilChanged() }

    private val mPlayer = MutableLiveData<Player?>()
    private var mVideoUri: String? = null

    override fun onCleared() {
        mPlayer.value?.release()
    }

    fun render(state: PlayerItemState.Video) {
        if (mVideoUri == state.uri) {
            return
        }

        mPlayer.value?.release()
        mPlayer.value = null

        val player = scope.get<ExoPlayer>()
        val dataSourceFactory = DefaultDataSourceFactory(
            scope.androidApplication(),
            Util.getUserAgent(scope.androidApplication(), "Light")
        )
        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(state.uri))

        player.prepare(videoSource)
        player.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                super.onTimelineChanged(timeline, reason)
            }
        })

        mPlayer.value = player
    }

    fun hide() {
        mPlayer.value?.release()
        mPlayer.value = null
    }

}