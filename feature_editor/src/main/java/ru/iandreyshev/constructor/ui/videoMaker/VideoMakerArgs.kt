package ru.iandreyshev.constructor.ui.videoMaker

import ru.iandreyshev.constructor.domain.course.CourseDraftId
import ru.iandreyshev.constructor.domain.video.VideoDraftId
import java.io.Serializable

data class VideoMakerArgs(
    val courseDraftId: CourseDraftId,
    val videoDraftId: VideoDraftId
) : Serializable
