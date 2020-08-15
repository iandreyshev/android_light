package ru.iandreyshev.constructor.ui.videoMaker

import com.google.android.exoplayer2.ExoPlayer

data class VideoMakerViewState(
    val isLoading: Boolean,
    val title: String,
    val player: ExoPlayer?,
    val duration: Int,
    val position: Int
) {

    val hasVideo: Boolean = player != null

}
