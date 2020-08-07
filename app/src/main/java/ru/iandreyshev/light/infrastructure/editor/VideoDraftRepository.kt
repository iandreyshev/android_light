package ru.iandreyshev.light.infrastructure.editor

import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.editor.files.ICourseDraftFilesStorage
import ru.iandreyshev.constructor.domain.video.IVideoDraftRepository
import ru.iandreyshev.constructor.domain.video.VideoDraftId
import ru.iandreyshev.constructor.domain.video.draft.VideoDraft
import ru.iandreyshev.constructor.utils.newUID
import timber.log.Timber

class VideoDraftRepository(
    private val id: VideoDraftId,
    private val courseRepository: ICourseDraftRepository,
    private val courseDraftFilesProvider: ICourseDraftFilesStorage
) : IVideoDraftRepository {

    private val mFiles by lazy {
        courseDraftFilesProvider.getVideoFiles(id)
    }

    override suspend fun get(): VideoDraft {
        return courseRepository.getItems()
            .mapNotNull { it.asTypeOrNull<DraftItem.Video>()?.draft }
            .firstOrNull { it.id == id }
            ?: VideoDraft(VideoDraftId(newUID()))
    }

    override suspend fun getVideoFilePath(): String {
        return mFiles.videoSourceFilePath
    }

    override suspend fun save(draft: VideoDraft) {
        courseRepository.add(DraftItem.Video(draft))
    }

    override suspend fun release() {
        Timber.d("Release video draft repository")
    }

}