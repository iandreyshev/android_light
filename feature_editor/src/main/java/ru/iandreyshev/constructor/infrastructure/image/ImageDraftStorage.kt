package ru.iandreyshev.constructor.infrastructure.image

import android.content.Context
import android.net.Uri
import ru.iandreyshev.constructor.domain.image.ImageDraftId
import ru.iandreyshev.constructor.domain.image.ImageSource
import ru.iandreyshev.constructor.infrastructure.ICourseDraftStorage
import timber.log.Timber
import java.io.File

class ImageDraftStorage(
    private val context: Context,
    private val courseStorage: ICourseDraftStorage,
    private val imageId: ImageDraftId
) : IImageDraftStorage {

    private val mImageFolderName: String
        get() = "image_${imageId.value}"

    override fun getPhotoSource(): ImageSource {
        val draftFile = getOrCreateDraftFile()
        val draftFilePath = draftFile.path

        return ImageSource(draftFilePath)
    }

    override fun createGallerySource(uri: Uri): ImageSource? {
        val draftFile = getOrCreateDraftFile()

        val resolver = context.contentResolver
        val galleryFileInput = resolver.openInputStream(uri)
            ?: kotlin.run { return null }

        galleryFileInput.copyTo(draftFile.outputStream())

        return ImageSource(filePath = draftFile.path)
    }

    override fun clear() {
        try {
            val courseFolder = courseStorage.getOrCreateCourseFolder()
            val imageFolder = File(courseFolder, mImageFolderName)

            imageFolder.deleteRecursively()
        } catch (ex: Exception) {
            Timber.d(ex)
        }
    }

    private fun getOrCreateDraftFile(): File {
        val courseFolder = courseStorage.getOrCreateCourseFolder()
        val imageFolder = File(courseFolder, mImageFolderName)
        imageFolder.mkdirs()

        return File(
            imageFolder,
            IMAGE_FILE_NAME
        )
    }

    companion object {
        // FIXME: 8/14/2020 Remove fixed extension
        private const val IMAGE_FILE_NAME = "source.png"
    }

}
