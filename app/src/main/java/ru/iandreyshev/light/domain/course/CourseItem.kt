package ru.iandreyshev.light.domain.course

sealed class CourseItem {
    class Quiz(val quiz: ru.iandreyshev.light.domain.quizMaker.Quiz) : CourseItem()
    class Image(val image: ru.iandreyshev.light.domain.imageMaker.Image) : CourseItem()
    class Video(val video: ru.iandreyshev.light.domain.videoMaker.Video) : CourseItem()
}
