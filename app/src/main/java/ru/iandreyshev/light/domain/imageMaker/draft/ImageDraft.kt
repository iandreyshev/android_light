package ru.iandreyshev.light.domain.imageMaker.draft

import ru.iandreyshev.light.domain.imageMaker.ImageSource

class ImageDraft {

    var text: String? = null
    var source: ImageSource? = null
    val fileName: String
        get() = source?.filePath.orEmpty()

}
