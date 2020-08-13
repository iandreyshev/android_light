package ru.iandreyshev.constructor.infrastructure

import android.content.Context
import ru.iandreyshev.constructor.domain.course.CourseDraftId
import timber.log.Timber
import java.io.File
import java.lang.Exception

class CourseDraftStorage(
    private val courseId: CourseDraftId,
    private val context: Context
) : ICourseDraftStorage {

    override fun getOrCreateCourseFolder(): File {
        val draftsFolder = File(
            context.filesDir,
            DRAFTS_FOLDER_PATH
        )

        if (!draftsFolder.exists()) {
            draftsFolder.mkdirs()
        }

        val draftFolder = File(draftsFolder, courseId.value)

        if (!draftFolder.exists()) {
            draftFolder.mkdirs()
        }

        return draftFolder
    }

    override fun clear() {
        try {
            getOrCreateCourseFolder().deleteRecursively()
        } catch (ex: Exception) {
            Timber.d(ex)
        }
    }

    companion object {
        private const val DRAFTS_FOLDER_PATH = "drafts"
    }

}
