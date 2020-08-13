package ru.iandreyshev.constructor.ui.imageMaker

import ru.iandreyshev.constructor.domain.image.ImageSource

sealed class ImageMakerEvent {
    class ShowError(val text: String) : ImageMakerEvent()
    class TakePhoto(val imageSource: ImageSource.Photo) : ImageMakerEvent()
    object Exit : ImageMakerEvent()
}
