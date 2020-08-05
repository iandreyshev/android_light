package ru.iandreyshev.constructor.domain.image.draft

class ImageDraft {

    var text: String? = null
    var source: ImageSource? = null
    val fileName: String
        get() = source?.filePath.orEmpty()

}
