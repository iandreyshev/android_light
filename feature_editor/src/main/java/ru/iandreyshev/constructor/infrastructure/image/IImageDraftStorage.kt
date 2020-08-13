package ru.iandreyshev.constructor.infrastructure.image

import android.net.Uri
import ru.iandreyshev.constructor.domain.editor.files.ImageFiles
import ru.iandreyshev.constructor.domain.image.ImageSource

interface IImageDraftStorage {
    fun getFiles(): ImageFiles
    fun save(uri: Uri): ImageSource.Gallery?
    fun clear()
}
