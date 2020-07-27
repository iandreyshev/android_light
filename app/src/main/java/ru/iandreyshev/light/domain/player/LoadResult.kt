package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.domain.course.CourseItem

sealed class LoadResult {
    class Success(val item: CourseItem, val itemsCount: Int) : LoadResult()
    object UnknownError : LoadResult()
}
