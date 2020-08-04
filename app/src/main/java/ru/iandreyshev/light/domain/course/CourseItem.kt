package ru.iandreyshev.light.domain.course

import ru.iandreyshev.light.domain.imageMaker.ImageSource
import ru.iandreyshev.light.domain.quizMaker.Question
import ru.iandreyshev.light.domain.videoMaker.VideoSource

sealed class CourseItem {

    class Quiz(
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

