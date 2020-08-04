package ru.iandreyshev.light.ui.player.video

import android.view.View
import androidx.core.view.isVisible
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.lay_player_video_view.view.*
import kotlinx.android.synthetic.main.lay_player_view_view_control_view.view.*

class VideoViewViewController(
    private val view: View,
    private val onBack: () -> Unit,
    private val onForward: () -> Unit
) {

    fun render(player: Player?): Unit = with(view) {
        if (player == null) {
            isVisible = false
        } else {
            isVisible = true
            playerView.player = player
            backButton.setOnClickListener { onBack() }
            forwardButton.setOnClickListener { onForward() }
        }
    }

}
