package ru.iandreyshev.constructor.domain.video.draft

import ru.iandreyshev.constructor.domain.video.VideoDraftId
import ru.iandreyshev.constructor.domain.video.VideoSource

data class VideoDraft(
    val id: VideoDraftId,
    val source: VideoSource? = null
)
