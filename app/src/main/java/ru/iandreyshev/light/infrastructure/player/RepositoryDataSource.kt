package ru.iandreyshev.light.infrastructure.player

import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId
import ru.iandreyshev.player_core.course.ICourseRepository
import ru.iandreyshev.player_core.course.IPlayerDataSource

class RepositoryDataSource(
    private val courseId: CourseId,
    private val repository: ICourseRepository
) : IPlayerDataSource {

    override fun getCourse(): Course? {
        return repository.getCourse(courseId)
    }

}