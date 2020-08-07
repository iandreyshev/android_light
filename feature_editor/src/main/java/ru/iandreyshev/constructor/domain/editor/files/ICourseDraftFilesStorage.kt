package ru.iandreyshev.constructor.domain.editor.files

import ru.iandreyshev.constructor.domain.image.ImageDraftId
import ru.iandreyshev.constructor.domain.video.VideoDraftId

interface ICourseDraftFilesStorage {
    fun getImageFiles(id: ImageDraftId): ImageFiles
    fun getVideoFiles(id: VideoDraftId): VideoFiles
    fun delete(path: String)
}
