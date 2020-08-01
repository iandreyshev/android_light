package ru.iandreyshev.light.infrastructure.editor

import ru.iandreyshev.light.domain.course.CourseItem
import ru.iandreyshev.light.domain.quizMaker.IQuizMakerRepository

class QuizMakerRepository : IQuizMakerRepository {

    override suspend fun get(): CourseItem.Quiz? {
        return null
    }

    override suspend fun save(quiz: CourseItem.Quiz) {
    }

}