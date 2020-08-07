package ru.iandreyshev.constructor.infrastructure

import android.content.Context
import java.io.File
import ru.iandreyshev.constructor.domain.course.CourseDraftId
import ru.iandreyshev.constructor.domain.editor.files.ICourseDraftFilesStorage
import ru.iandreyshev.constructor.domain.editor.files.ImageFiles
import ru.iandreyshev.constructor.domain.editor.files.VideoFiles
import ru.iandreyshev.constructor.domain.image.ImageDraftId
import ru.iandreyshev.constructor.domain.video.VideoDraftId
import timber.log.Timber
import java.lang.Exception

class InternalStorageCourseDraftFilesStorage(
    private val context: Context,
    private val courseId: CourseDraftId
) : ICourseDraftFilesStorage {

    override fun getImageFiles(id: ImageDraftId): ImageFiles {
        val folder = File(getOrCreateCourseFolder(), "image_${id.value}")
        folder.mkdirs()

        val sourceFile = File(folder, "source.PNG")

        return ImageFiles(
            folderPath = folder.path,
            sourceFilePath = sourceFile.path
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

    override fun delete(path: String) {
        try {
            File(path).deleteRecursively()
        } catch (ex: Exception) {
            Timber.d(ex)
        }
    }

    companion object {
        private const val DRAFTS_FOLDER_PATH = "drafts"
    }

}