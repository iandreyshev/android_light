package ru.iandreyshev.constructor.infrastructure.image

import android.net.Uri
import ru.iandreyshev.constructor.domain.image.ImageSource

interface IImageDraftStorage {
    fun getPhotoSource(): ImageSource?
    fun createGallerySource(uri: Uri): ImageSource?
    fun clear()
}
