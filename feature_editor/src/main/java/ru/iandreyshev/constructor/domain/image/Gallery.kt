package ru.iandreyshev.constructor.domain.image

sealed class ImageSource {
    data class Gallery(val filePath: String) : ImageSource()
    data class Photo(val filePath: String) : ImageSource()
}
