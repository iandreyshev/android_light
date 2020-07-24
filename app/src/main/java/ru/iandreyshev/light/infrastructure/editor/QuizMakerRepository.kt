package ru.iandreyshev.light.infrastructure.editor

import ru.iandreyshev.light.domain.quizMaker.IQuizMakerRepository
import ru.iandreyshev.light.domain.quizMaker.Quiz

class QuizMakerRepository : IQuizMakerRepository {

    override suspend fun get(): Quiz? {
        return null
    }

    override suspend fun save(quiz: Quiz) {
    }

}