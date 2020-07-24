package ru.iandreyshev.light.domain.quizMaker

interface IQuizMakerRepository {
    suspend fun get(): Quiz?
    suspend fun save(quiz: Quiz)
}
