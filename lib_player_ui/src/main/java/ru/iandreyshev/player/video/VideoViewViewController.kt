package ru.iandreyshev.player.video

import android.net.Uri
import android.view.View
import androidx.core.view.isVisible
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.lay_player_video_view.view.*
import kotlinx.android.synthetic.main.lay_player_view_view_control_view.view.*
import ru.iandreyshev.player_core.course.PlayerItemState

internal class VideoViewViewController(
    private val view: View,
    private val onBack: () -> Unit,
    private val onForward: () -> Unit
) {

    private var mVideoUri: String = ""
    private var mPlayer: ExoPlayer? = null

    fun render(state: PlayerItemState.Video?): Unit = with(view) {
        val newUri: String? = state?.uri

        if (newUri == null) {
            isVisible = false
            return@with
        }

        if (state.uri == mVideoUri) {
            return@with
        }

        isVisible = true
        mVideoUri = newUri

        mPlayer = newPlayer(newUri)
        playerView.player = mPlayer
        backButton.setOnClickListener { onBack() }
        forwardButton.setOnClickListener { onForward() }
    }

    fun pause() {
        if (mPlayer?.playbackState == Player.STATE_READY
            && mPlayer?.playWhenReady == true
        ) {
            mPlayer?.playWhenReady = false
        }
    }

    private fun newPlayer(uri: String): ExoPlayer {
        return SimpleExoPlayer.Builder(view.context)
            .build()
            .apply {
                val dataSourceFactory = DefaultDataSourceFactory(
                    view.context,
                    Util.getUserAgent(view.context, "Light")
                )
                val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(uri))

                prepare(videoSource)
            }
    }

}
