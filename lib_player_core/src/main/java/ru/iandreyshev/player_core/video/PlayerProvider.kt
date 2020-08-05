package ru.iandreyshev.player_core.video

import com.google.android.exoplayer2.ExoPlayer
import ru.iandreyshev.player_core.course.PlayerItemState

typealias PlayerProvider = (PlayerItemState.Video) -> ExoPlayer
