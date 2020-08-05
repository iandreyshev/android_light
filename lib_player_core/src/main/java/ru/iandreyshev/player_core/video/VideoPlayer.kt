package ru.iandreyshev.player_core.video

import com.google.android.exoplayer2.ExoPlayer
import ru.iandreyshev.player_core.course.PlayerItemState

class VideoPlayer(
    private val playerProvider: PlayerProvider
) {

    var videoPlayerListener: (ExoPlayer) -> Unit = {}
    var videoPlayer: ExoPlayer? = null
        private set

    private var mVideoUri: String? = null

    fun dispose() {
        videoPlayer?.release()
    }

    fun render(state: PlayerItemState.Video) {
        if (mVideoUri == state.uri) {
            return
        }

        videoPlayer?.release()
        videoPlayer = null

        val player = playerProvider(state)

        videoPlayer = player
        videoPlayer?.let(videoPlayerListener)
    }

    fun hide() {
        videoPlayer?.release()
        videoPlayer = null
    }

}
