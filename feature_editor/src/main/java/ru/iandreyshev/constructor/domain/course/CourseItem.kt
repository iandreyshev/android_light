package ru.iandreyshev.constructor.domain.course

import ru.iandreyshev.constructor.domain.quiz.Question

sealed class CourseItem {

    class Image() : CourseItem()

    class Video() : CourseItem()

    class Quiz(
        val questions: List<Question>
    ) : CourseItem()

}