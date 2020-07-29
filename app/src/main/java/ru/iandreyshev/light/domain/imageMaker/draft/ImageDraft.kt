package ru.iandreyshev.light.domain.imageMaker.draft

import ru.iandreyshev.light.domain.imageMaker.ImageDuration
import ru.iandreyshev.light.domain.imageMaker.ImageSource

class ImageDraft {

    var duration = ImageDuration.INFINITELY
        private set

    var text: String? = null
    var imageSource: ImageSource? = null
    val fileName: String
        get() = imageSource?.filePath.orEmpty()

    fun switchDuration(): ImageDuration {
        return when (duration) {
            ImageDuration.INFINITELY -> ImageDuration.SEC_3
            ImageDuration.SEC_3 -> ImageDuration.SEC_5
            ImageDuration.SEC_5 -> ImageDuration.SEC_10
            ImageDuration.SEC_10 -> ImageDuration.INFINITELY
        }.also {
            this.duration = it
        }
    }

}
