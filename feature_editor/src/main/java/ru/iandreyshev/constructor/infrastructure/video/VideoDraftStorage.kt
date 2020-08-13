package ru.iandreyshev.constructor.infrastructure.video

import android.content.Context
import android.net.Uri
import ru.iandreyshev.constructor.domain.video.VideoDraftId
import ru.iandreyshev.constructor.domain.video.VideoSource
import ru.iandreyshev.constructor.infrastructure.ICourseDraftStorage
import timber.log.Timber
import java.io.File

class VideoDraftStorage(
    private val context: Context,
    private val courseStorage: ICourseDraftStorage,
    private val videoId: VideoDraftId
) : IVideoDraftStorage {

    private val mVideoFolderName: String
        get() = "video_${videoId.value}"

    override fun createGallerySource(uri: Uri): VideoSource? {
        val draftFile = getOrCreateDraftFile()

        val resolver = context.contentResolver
        val galleryFileInput = resolver.openInputStream(uri)
            ?: kotlin.run { return null }

        galleryFileInput.copyTo(draftFile.outputStream())

        return VideoSource(draftFile.path)
    }

    override fun clear() {
        try {
            val courseFolder = courseStorage.getOrCreateCourseFolder()
            val videoFolder = File(courseFolder, mVideoFolderName)

            videoFolder.deleteRecursively()
        } catch (ex: Exception) {
            Timber.d(ex)
        }
    }

    private fun getOrCreateDraftFile(): File {
        val courseFolder = courseStorage.getOrCreateCourseFolder()
        val videoFolder = File(courseFolder, mVideoFolderName)
        videoFolder.mkdirs()

        return File(
            videoFolder,
            VIDEO_FILE_NAME
        )
    }

    companion object {
        // FIXME: 8/14/2020 Remove fixed extension
        private const val VIDEO_FILE_NAME = "source.mp4"
    }

}
