package ru.iandreyshev.constructor.domain.image

import android.net.Uri
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft

interface IImageDraftRepository {
    suspend fun get(): ImageDraft
    suspend fun getPhotoSource(): ImageSource.Photo
    suspend fun getGallerySource(uri: Uri): ImageSource.Gallery?
    suspend fun save(draft: ImageDraft): ImageDraftResult
    suspend fun release()
}
