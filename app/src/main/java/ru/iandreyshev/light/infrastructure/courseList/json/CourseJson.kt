package ru.iandreyshev.light.infrastructure.courseList.json

import ru.iandreyshev.core_utils.exhaustive
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId
import ru.iandreyshev.player_core.course.CourseItem
import ru.iandreyshev.player_core.image.ImageSource
import ru.iandreyshev.player_core.quiz.*
import ru.iandreyshev.player_core.video.VideoSource
import java.util.*
import kotlinx.serialization.*

@Serializable
data class CourseListJson(val courses: List<CourseJson>)

fun Course.toJson(): CourseJson {
    val images = mutableListOf<ImageJson>()
    val videos = mutableListOf<VideoJson>()
    val quizzes = mutableListOf<QuizJson>()

    items.forEachIndexed { index, courseItem ->
        when (courseItem) {
            is CourseItem.Quiz -> quizzes.add(courseItem.toJson(index))
            is CourseItem.Image -> images.add(courseItem.toJson(index))
            is CourseItem.Video -> videos.add(courseItem.toJson(index))
        }.exhaustive
    }

    return CourseJson(
        id = id.value,
        title = title,
        creationDate = creationDate.time,
        images = images,
        videos = videos,
        quizzes = quizzes
    )
}

private fun CourseItem.Image.toJson(position: Int) =
    ImageJson(
        description = description,
        source = source.uri,
        positionInCourse = position
    )

private fun CourseItem.Video.toJson(position: Int) =
    VideoJson(
        source = source.filePath,
        positionInCourse = position
    )

private fun CourseItem.Quiz.toJson(position: Int) =
    QuizJson(
        id = id.value,
        questions = questions.map { it.toJson() },
        positionInCourse = position
    )

private fun Question.toJson() =
    QuestionJson(
        id = id.value,
        text = text,
        position = position,
        isMultipleMode = isMultipleMode,
        variants = variants.map { it.toJson() },
        result = result
    )

private fun Variant.toJson() =
    VariantJson(
        id = id.value,
        text = text,
        isCorrect = isCorrect,
        isSelectedAsCorrect = isSelectedAsCorrect
    )

fun CourseJson.toCourse(): Course {
    val itemsMap = mutableMapOf<Int, CourseItem>()

    itemsMap += images.associateBy { it.positionInCourse }
        .mapValues { it.value.toCourseItem() }

    itemsMap += videos.associateBy { it.positionInCourse }
        .mapValues { it.value.toCourseItem() }

    itemsMap += quizzes.associateBy { it.positionInCourse }
        .mapValues { it.value.toCourseItem() }

    return Course(
        id = CourseId(id),
        title = title,
        creationDate = Date(creationDate),
        items = itemsMap.toSortedMap()
            .values.toList()
    )
}

private fun ImageJson.toCourseItem() =
    CourseItem.Image(
        description = description,
        source = ImageSource(source)
    )

private fun VideoJson.toCourseItem() =
    CourseItem.Video(
        source = VideoSource(source)
    )

private fun QuizJson.toCourseItem() =
    CourseItem.Quiz(
        id = QuizId(id),
        questions = questions.map { it.toCourseItem() }
    )

private fun QuestionJson.toCourseItem() =
    Question(
        id = QuestionId(id),
        text = text,
        position = position,
        isMultipleMode = isMultipleMode,
        result = result,
        variants = variants.map { it.toCourseItem() }
    )

private fun VariantJson.toCourseItem() =
    Variant(
        id = VariantId(id),
        text = text,
        isSelectedAsCorrect = isSelectedAsCorrect,
        isCorrect = isCorrect
    )

@Serializable
data class CourseJson(
    val id: String,
    val title: String,
    val creationDate: Long,
    val images: List<ImageJson>,
    val videos: List<VideoJson>,
    val quizzes: List<QuizJson>
)

@Serializable
data class ImageJson(
    val description: String?,
    val source: String,
    val positionInCourse: Int
)

@Serializable
data class VideoJson(
    val source: String,
    val positionInCourse: Int
)

@Serializable
data class QuizJson(
    val id: String,
    val questions: List<QuestionJson>,
    val positionInCourse: Int
)

@Serializable
data class QuestionJson(
    val id: String,
    val text: String,
    val position: Int,
    val isMultipleMode: Boolean,
    val variants: List<VariantJson>,
    val result: QuestionResult
)

@Serializable
data class VariantJson(
    val id: String,
    val text: String,
    val isSelectedAsCorrect: Boolean,
    val isCorrect: Boolean
)
