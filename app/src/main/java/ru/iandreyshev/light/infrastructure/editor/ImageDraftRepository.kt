package ru.iandreyshev.light.infrastructure.editor

import android.net.Uri
import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.infrastructure.image.IImageDraftStorage
import ru.iandreyshev.constructor.domain.image.*
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft
import ru.iandreyshev.constructor.utils.newUID
import timber.log.Timber

class ImageDraftRepository(
    private val id: ImageDraftId,
    private val courseRepository: ICourseDraftRepository,
    private val storage: IImageDraftStorage
) : IImageDraftRepository {

    override suspend fun get(): ImageDraft {
        return courseRepository.getItems()
            .mapNotNull { it.asTypeOrNull<DraftItem.Image>()?.draft }
            .firstOrNull { it.id == id }
            ?: ImageDraft(ImageDraftId(newUID()))
    }

    override suspend fun getPhotoSource(): ImageSource? {
        return storage.getPhotoSource()
    }

    override suspend fun getGallerySource(uri: Uri): ImageSource? {
        return storage.createGallerySource(uri)
    }

    override suspend fun save(draft: ImageDraft): ImageDraftResult {
        if (draft.source == null) {
            return ImageDraftResult.Error(ImageDraftError.SOURCE_NOT_FOUND)
        }

        courseRepository.add(DraftItem.Image(draft))

        return ImageDraftResult.Success
    }

    override suspend fun clear() {

        Timber.d("Delete image draft files")
        storage.clear()
    }

}
