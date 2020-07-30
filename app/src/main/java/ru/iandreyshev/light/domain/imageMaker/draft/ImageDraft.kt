package ru.iandreyshev.light.domain.imageMaker.draft

import ru.iandreyshev.light.domain.imageMaker.ImageSource

class ImageDraft {

    var text: String? = null
    var imageSource: ImageSource? = null
    val fileName: String
        get() = imageSource?.filePath.orEmpty()

}
