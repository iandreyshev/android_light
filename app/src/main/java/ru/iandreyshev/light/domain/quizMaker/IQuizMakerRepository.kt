package ru.iandreyshev.light.domain.quizMaker

import ru.iandreyshev.light.domain.course.CourseItem

interface IQuizMakerRepository {
    suspend fun get(): CourseItem.Quiz?
    suspend fun save(quiz: CourseItem.Quiz)
}
