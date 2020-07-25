package ru.iandreyshev.light.domain.imageMaker.draft

import ru.iandreyshev.light.domain.imageMaker.ImageDuration

class ImageDraft {

    var duration = ImageDuration.SEC_3
        private set

    var text: String = ""
        private set

    fun switchDuration(): ImageDuration {
        return when (duration) {
            ImageDuration.SEC_3 -> ImageDuration.SEC_5
            ImageDuration.SEC_5 -> ImageDuration.SEC_10
            ImageDuration.SEC_10 -> ImageDuration.SEC_3
        }.also {
            this.duration = it
        }
    }

    fun setText(text: String) {
        this.text = text
    }

}
