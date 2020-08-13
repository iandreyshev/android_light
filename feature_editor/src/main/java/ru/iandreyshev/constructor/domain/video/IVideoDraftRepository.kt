package ru.iandreyshev.constructor.domain.video

import android.net.Uri
import ru.iandreyshev.constructor.domain.video.draft.VideoDraft

interface IVideoDraftRepository {
    suspend fun get(): VideoDraft
    suspend fun getGallerySource(uri: Uri): VideoSource?
    suspend fun save(draft: VideoDraft)
    suspend fun release()
}
