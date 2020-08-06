package ru.iandreyshev.constructor.domain.image

import ru.iandreyshev.constructor.domain.image.draft.ImageDraft

interface IImageDraftRepository {
    suspend fun get(): ImageDraft
    suspend fun getImageFilePath(): String
    suspend fun save(draft: ImageDraft)
    suspend fun release()
}
