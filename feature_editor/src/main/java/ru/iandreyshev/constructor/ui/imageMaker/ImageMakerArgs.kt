package ru.iandreyshev.constructor.ui.imageMaker

import ru.iandreyshev.constructor.domain.course.CourseDraftId
import ru.iandreyshev.constructor.domain.image.ImageDraftId
import java.io.Serializable

class ImageMakerArgs(
    val courseDraftId: CourseDraftId,
    val imageDraftId: ImageDraftId
) : Serializable
