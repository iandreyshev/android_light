package ru.iandreyshev.light.infrastructure.courseList

import android.content.Context
import ru.iandreyshev.constructor.domain.course.CourseDraftId
import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.image.ImageSource
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft
import ru.iandreyshev.constructor.domain.quiz.draft.QuizDraft
import ru.iandreyshev.constructor.domain.video.draft.VideoDraft
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId
import ru.iandreyshev.player_core.course.CourseItem
import ru.iandreyshev.player_core.quiz.*
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.util.*

class CourseStorage(
    private val context: Context
) : ICourseStorage {

    override fun save(draft: CourseDraft): Course {
        val id = CourseId(draft.id.value)
        val courseFolder = getOrCreateCourseFolder(id)

        return convert(draft, courseFolder)
    }

    override fun delete(courseId: CourseId) {
        try {
            getOrCreateCourseFolder(courseId).deleteRecursively()
        } catch (ex: Exception) {
            Timber.d(ex)
        }
    }

    private fun getOrCreateCourseFolder(courseId: CourseId): File {
        val rootFolder = File(context.filesDir, ROOT_FOLDER_PATH)

        if (!rootFolder.exists()) {
            rootFolder.mkdirs()
        }

        val courseFolderName = "$COURSE_FOLDER_PREFIX${courseId.value}"
        val courseFolder = File(rootFolder, courseFolderName)

        if (!courseFolder.exists()) {
            courseFolder.mkdirs()
        }

        return courseFolder
    }

    private fun convert(draft: CourseDraft, folder: File): Course {
        val id = draft.id.asCourseId()

        return Course(
            id = id,
            title = draft.title,
            creationDate = Date(),
            items = draft.items.mapNotNull { item ->
                when (item) {
                    is DraftItem.Quiz -> convertQuiz(item.draft)
                    is DraftItem.Image -> convertImage(item.draft, folder)
                    is DraftItem.Video -> convertVideo(item.draft, folder)
                }
            }
        )
    }

    private fun convertQuiz(quiz: QuizDraft): CourseItem.Quiz {
        return CourseItem.Quiz(
            id = QuizId(quiz.id.value),
            questions = quiz.questions
                .mapIndexed { pos, questionDraft ->
                    Question(
                        id = QuestionId(questionDraft.id.value),
                        text = questionDraft.text,
                        variants = questionDraft.variants.map { variantDraft ->
                            Variant(
                                id = VariantId(variantDraft.id.value),
                                text = variantDraft.text,
                                isCorrect = variantDraft.isCorrect,
                                isSelectedAsCorrect = true
                            )
                        },
                        isMultipleMode = questionDraft.isMultipleMode,
                        position = pos,
                        result = QuestionResult.UNDEFINED
                    )
                })
    }

    private fun convertImage(image: ImageDraft, courseFolder: File): CourseItem.Image? {
        val imageFolder = File(courseFolder, image.id.value)
        imageFolder.mkdirs()

        val draftSourceFile = when (val source = image.source) {
            is ImageSource.Gallery -> File(source.filePath)
            is ImageSource.Photo -> File(source.filePath)
            null -> {
                Timber.d("Image ${image.id} has null source")
                return null
            }
        }
        val sourceFile = File(imageFolder, IMAGE_FILE_NAME)
        val sourceFilePath = sourceFile.path ?: kotlin.run {
            Timber.d("Image ${image.id} has null source file path")
            return null
        }

        draftSourceFile.copyTo(sourceFile)

        return CourseItem.Image(
            image.text,
            ru.iandreyshev.player_core.image.ImageSource(sourceFilePath)
        )
    }

    private fun convertVideo(video: VideoDraft, courseFolder: File): CourseItem.Video? {
        val videoFolder = File(courseFolder, video.id.value)
        videoFolder.mkdirs()

        val draftSourceFile = video.source?.filePath
            ?.let { File(it) }
            ?: kotlin.run {
                Timber.d("Video ${video.id} has null source")
                return null
            }

        val sourceFile = File(videoFolder, VIDEO_FILE_NAME)
        val sourceFilePath = sourceFile.path ?: kotlin.run {
            Timber.d("Video ${video.id} has null source file path")
            return null
        }

        draftSourceFile.copyTo(sourceFile)

        return CourseItem.Video(
            ru.iandreyshev.player_core.video.VideoSource(sourceFilePath)
        )
    }

    private fun CourseDraftId.asCourseId() = CourseId(value)

    companion object {
        private const val ROOT_FOLDER_PATH = "courses"
        private const val COURSE_FOLDER_PREFIX = "course_"

        // FIXME: 8/14/2020 Remove fixed extensions
        private const val IMAGE_FILE_NAME = "source.png"
        private const val VIDEO_FILE_NAME = "source.mp4"
    }

}
