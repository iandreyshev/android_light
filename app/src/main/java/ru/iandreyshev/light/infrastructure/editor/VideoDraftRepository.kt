package ru.iandreyshev.light.infrastructure.editor

import android.net.Uri
import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.infrastructure.video.IVideoDraftStorage
import ru.iandreyshev.constructor.domain.video.IVideoDraftRepository
import ru.iandreyshev.constructor.domain.video.VideoDraftId
import ru.iandreyshev.constructor.domain.video.VideoSource
import ru.iandreyshev.constructor.domain.video.draft.VideoDraft
import ru.iandreyshev.constructor.utils.newUID
import timber.log.Timber

class VideoDraftRepository(
    private val id: VideoDraftId,
    private val courseRepository: ICourseDraftRepository,
    private val storage: IVideoDraftStorage
) : IVideoDraftRepository {

    override suspend fun get(): VideoDraft {
        return courseRepository.getItems()
            .mapNotNull { it.asTypeOrNull<DraftItem.Video>()?.draft }
            .firstOrNull { it.id == id }
            ?: VideoDraft(VideoDraftId(newUID()))
    }

    override suspend fun getGallerySource(uri: Uri): VideoSource? {
        return storage.createGallerySource(uri)
    }

    override suspend fun save(draft: VideoDraft) {
        courseRepository.add(DraftItem.Video(draft))
    }

    override suspend fun clear() {
        Timber.d("Release video $id draft files")
        storage.clear()
    }

}
