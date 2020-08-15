package ru.iandreyshev.light.infrastructure.courseList

import android.content.Context
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import ru.iandreyshev.constructor.domain.course.CourseDraftId
import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.image.ImageSource
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft
import ru.iandreyshev.constructor.domain.quiz.draft.QuizDraft
import ru.iandreyshev.constructor.domain.video.draft.VideoDraft
import ru.iandreyshev.light.infrastructure.courseList.json.CourseListJson
import ru.iandreyshev.light.infrastructure.courseList.json.toCourse
import ru.iandreyshev.light.infrastructure.courseList.json.toJson
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

    override fun add(draft: CourseDraft): Course {
        val id = draft.id.asCourseId()

        val course = Course(
            id = id,
            title = draft.title,
            creationDate = Date(),
            items = saveItems(id, draft.items)
        )

        saveStructure(course)

        return course
    }

    override fun list(): List<Course> {
        return try {
            val courseListFile = getOrCreateCourseListFile()

            val json = Json(JsonConfiguration.Stable)
            val jsonData = json.parse(CourseListJson.serializer(), courseListFile.readText())

            jsonData.courses.map { it.toCourse() }
        } catch (ex: Exception) {
            Timber.e(ex)
            emptyList()
        }
    }

    override fun remove(courseId: CourseId) {
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

    private fun getOrCreateCourseListFile(): File {
        val rootFolder = File(context.filesDir, ROOT_FOLDER_PATH)

        if (!rootFolder.exists()) {
            rootFolder.mkdirs()
        }

        val file = File(rootFolder, COURSE_LIST_JSON_FILE_NAME)

        if (!file.exists()) {
            file.createNewFile()
        }

        return file
    }

    private fun saveItems(id: CourseId, items: List<DraftItem>): List<CourseItem> {
        val folder = getOrCreateCourseFolder(id)

        return items.mapNotNull { item ->
            when (item) {
                is DraftItem.Quiz -> convertToQuiz(item.draft)
                is DraftItem.Image ->
                    CourseItem.Image(
                        description = item.draft.description,
                        source = saveToFile(item.draft, folder)
                            ?: return@mapNotNull null
                    )
                is DraftItem.Video ->
                    CourseItem.Video(
                        source = saveToFile(item.draft, folder)
                            ?: return@mapNotNull null
                    )
            }
        }
    }

    private fun convertToQuiz(quiz: QuizDraft): CourseItem.Quiz {
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

    private fun saveToFile(
        image: ImageDraft,
        courseFolder: File
    ): ru.iandreyshev.player_core.image.ImageSource? {
        val imageFolder = File(courseFolder, image.id.value)
        imageFolder.mkdirs()

        val draftSourceFile = when (val source = image.source) {
            is ImageSource -> File(source.filePath)
            else -> {
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

        return ru.iandreyshev.player_core.image.ImageSource(sourceFilePath)
    }

    private fun saveToFile(
        video: VideoDraft,
        courseFolder: File
    ): ru.iandreyshev.player_core.video.VideoSource? {
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

        return ru.iandreyshev.player_core.video.VideoSource(sourceFilePath)
    }

    private fun saveStructure(course: Course) {
        val courses = list().toMutableList()
            .apply { add(course) }
            .map { it.toJson() }

        val json = Json(JsonConfiguration.Stable)
        val jsonString = json.stringify(CourseListJson.serializer(), CourseListJson(courses))

        val courseListFile = getOrCreateCourseListFile()

        courseListFile.writeText(jsonString)
    }

    private fun CourseDraftId.asCourseId() = CourseId(value)

    companion object {
        private const val ROOT_FOLDER_PATH = "courses"
        private const val COURSE_FOLDER_PREFIX = "course_"

        // FIXME: 8/14/2020 Remove fixed extensions
        private const val COURSE_LIST_JSON_FILE_NAME = "list.json"
        private const val IMAGE_FILE_NAME = "source.png"
        private const val VIDEO_FILE_NAME = "source.mp4"
    }

}
