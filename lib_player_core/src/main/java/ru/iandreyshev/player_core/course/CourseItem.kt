package ru.iandreyshev.player_core.course

import ru.iandreyshev.player_core.image.ImageSource
import ru.iandreyshev.player_core.quiz.Question
import ru.iandreyshev.player_core.quiz.QuizId
import ru.iandreyshev.player_core.video.VideoSource

sealed class CourseItem {

    class Quiz(
        val id: QuizId,
        val questions: List<Question>
    ) : CourseItem()

    class Image(
        val text: String?,
        val source: ImageSource
    ) : CourseItem()

    class Video(
        val source: VideoSource
    ) : CourseItem()

}

