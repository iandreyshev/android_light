package ru.iandreyshev.constructor.infrastructure.image

import android.net.Uri
import ru.iandreyshev.constructor.domain.editor.files.ImageFiles
import ru.iandreyshev.constructor.domain.image.ImageDraftId
import ru.iandreyshev.constructor.domain.image.ImageSource
import ru.iandreyshev.constructor.infrastructure.ICourseDraftStorage
import timber.log.Timber
import java.io.File

class ImageDraftStorage(
    private val courseStorage: ICourseDraftStorage,
    private val imageId: ImageDraftId
) : IImageDraftStorage {

    private val mImageFolderName: String
        get() = "image_${imageId.value}"

    override fun getFiles(): ImageFiles {
        val courseFolder = courseStorage.getOrCreateCourseFolder()
        val imageFolder = File(courseFolder, mImageFolderName)
        imageFolder.mkdirs()

        val sourceFile = File(
            imageFolder,
            IMAGE_FILE_NAME
        )

        return ImageFiles(
            folderPath = imageFolder.path,
            sourceFilePath = sourceFile.path
        )
    }

    override fun save(uri: Uri): ImageSource.Gallery? {
        val files = getFiles()
        val sourceFile = File(uri.path ?: return null)
        val draftFile = File(files.sourceFilePath)

        sourceFile.copyTo(draftFile)

        return ImageSource.Gallery(filePath = draftFile.path)
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

    companion object {
        private const val IMAGE_FILE_NAME = "source.PNG"
    }

}
