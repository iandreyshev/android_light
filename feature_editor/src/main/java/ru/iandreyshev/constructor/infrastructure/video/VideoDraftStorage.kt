package ru.iandreyshev.constructor.infrastructure.video

import android.net.Uri
import ru.iandreyshev.constructor.domain.editor.files.VideoFiles
import ru.iandreyshev.constructor.domain.video.VideoDraftId
import ru.iandreyshev.constructor.domain.video.VideoSource
import ru.iandreyshev.constructor.infrastructure.ICourseDraftStorage
import timber.log.Timber
import java.io.File

class VideoDraftStorage(
    private val courseStorage: ICourseDraftStorage,
    private val videoId: VideoDraftId
) : IVideoDraftStorage {

    private val mImageFolderName: String
        get() = "video_${videoId.value}"

    override fun getFiles(): VideoFiles {
        val courseFolder = courseStorage.getOrCreateCourseFolder()
        val videoFolder = File(courseFolder, mImageFolderName)
        videoFolder.mkdirs()

        val sourceFile = File(
            videoFolder,
            VIDEO_FILE_NAME
        )

        return VideoFiles(
            folderPath = videoFolder.path,
            sourceFilePath = sourceFile.path
        )
    }

    override fun save(uri: Uri): VideoSource? {
        val files = getFiles()
        val sourceFile = File(uri.path ?: return null)
        val draftFile = File(files.sourceFilePath)

        sourceFile.copyTo(draftFile)

        return VideoSource(filePath = draftFile.path)
    }

    override fun delete() {
        try {
            val courseFolder = courseStorage.getOrCreateCourseFolder()
            val videoFolder = File(courseFolder, mImageFolderName)

            videoFolder.deleteRecursively()
        } catch (ex: Exception) {
            Timber.d(ex)
        }
    }

    companion object {
        private const val VIDEO_FILE_NAME = "source.PNG"
    }

}
