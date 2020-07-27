package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.domain.course.Course

interface IPlayerDataSource {
    fun getCourse(): Course?
}
