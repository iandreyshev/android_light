package ru.iandreyshev.light.infrastructure.editor

import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.editor.files.ICourseDraftFilesProvider
import ru.iandreyshev.constructor.domain.image.IImageDraftRepository
import ru.iandreyshev.constructor.domain.image.ImageDraftId
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft
import ru.iandreyshev.constructor.utils.newUID
import timber.log.Timber

class ImageDraftRepository(
    private val id: ImageDraftId,
    private val courseRepository: ICourseDraftRepository,
    private val filesProvider: ICourseDraftFilesProvider
) : IImageDraftRepository {

    private val mFiles by lazy {
        filesProvider.getImageFiles(id)
    }

    override suspend fun get(): ImageDraft {
        return courseRepository.getItems()
            .mapNotNull { it.asTypeOrNull<DraftItem.Image>()?.draft }
            .firstOrNull { it.id == id }
            ?: ImageDraft(ImageDraftId(newUID()))
    }

    override suspend fun getImageFilePath(): String {
        return mFiles.imageSourceFilePath
    }

    override suspend fun save(draft: ImageDraft) {
        courseRepository.add(DraftItem.Image(draft))
    }

    override suspend fun release() {
        Timber.d("Release image draft repository")
    }

}
