package ru.iandreyshev.constructor.infrastructure.video

import android.net.Uri
import ru.iandreyshev.constructor.domain.video.VideoSource

interface IVideoDraftStorage {
    fun createGallerySource(uri: Uri): VideoSource?
    fun clear()
}
