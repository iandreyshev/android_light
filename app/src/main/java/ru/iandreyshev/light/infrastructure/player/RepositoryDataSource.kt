package ru.iandreyshev.light.infrastructure.player

import ru.iandreyshev.light.domain.course.Course
import ru.iandreyshev.light.domain.course.CourseId
import ru.iandreyshev.light.domain.course.ICourseRepository
import ru.iandreyshev.light.domain.player.IPlayerDataSource

class RepositoryDataSource(
    private val courseId: CourseId,
    private val repository: ICourseRepository
) : IPlayerDataSource {

    override fun getCourse(): Course? {
        return repository.getCourse(courseId)
    }

}