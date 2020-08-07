package ru.iandreyshev.light.infrastructure.editor

import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.editor.files.ICourseDraftFilesStorage
import ru.iandreyshev.constructor.domain.image.IImageDraftRepository
import ru.iandreyshev.constructor.domain.image.ImageDraftError
import ru.iandreyshev.constructor.domain.image.ImageDraftId
import ru.iandreyshev.constructor.domain.image.ImageDraftResult
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft
import ru.iandreyshev.constructor.utils.newUID
import timber.log.Timber

class ImageDraftRepository(
    private val id: ImageDraftId,
    private val courseRepository: ICourseDraftRepository,
    private val filesStorage: ICourseDraftFilesStorage
) : IImageDraftRepository {

    override suspend fun get(): ImageDraft {
        return courseRepository.getItems()
            .mapNotNull { it.asTypeOrNull<DraftItem.Image>()?.draft }
            .firstOrNull { it.id == id }
            ?: ImageDraft(ImageDraftId(newUID()))
    }

    override suspend fun getImageFilePath(): String {
        val files = filesStorage.getImageFiles(id)

        return files.sourceFilePath
    }

    override suspend fun save(draft: ImageDraft): ImageDraftResult {
        if (draft.source == null) {
            return ImageDraftResult.Error(ImageDraftError.SOURCE_NOT_FOUND)
        }

        courseRepository.add(DraftItem.Image(draft))

        return ImageDraftResult.Success
    }

    override suspend fun release() {
        Timber.d("Delete image draft files")
        val files = filesStorage.getImageFiles(id)
        filesStorage.delete(files.folderPath)
    }

}
