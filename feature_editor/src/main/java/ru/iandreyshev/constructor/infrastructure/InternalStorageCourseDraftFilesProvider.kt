package ru.iandreyshev.constructor.infrastructure

import android.content.Context
import java.io.File
import ru.iandreyshev.constructor.domain.course.CourseDraftId
import ru.iandreyshev.constructor.domain.editor.files.ICourseDraftFilesProvider
import ru.iandreyshev.constructor.domain.editor.files.ImageFiles
import ru.iandreyshev.constructor.domain.editor.files.VideoFiles
import ru.iandreyshev.constructor.domain.image.ImageDraftId
import ru.iandreyshev.constructor.domain.video.VideoDraftId

class InternalStorageCourseDraftFilesProvider(
    private val context: Context,
    private val courseId: CourseDraftId
) : ICourseDraftFilesProvider {

    override fun getImageFiles(id: ImageDraftId): ImageFiles {
        val imageFolder = File(getOrCreateCourseFolder(), "image_${id.value}")
        imageFolder.mkdirs()

        val imageSourceFile = File(imageFolder, "source.PNG")

        return ImageFiles(
            imageSourceFilePath = imageSourceFile.path
        )
    }

    override fun getVideoFiles(id: VideoDraftId): VideoFiles {
        TODO("Not yet implemented")
    }

    private fun getOrCreateCourseFolder(): File {
        val draftsFolder = File(context.filesDir, DRAFTS_FOLDER_PATH)

        if (!draftsFolder.exists()) {
            draftsFolder.mkdirs()
        }

        val draftFolder = File(draftsFolder, courseId.value)

        if (!draftFolder.exists()) {
            draftFolder.mkdirs()
        }

        return draftFolder
    }

    companion object {
        private const val DRAFTS_FOLDER_PATH = "drafts"
    }

}