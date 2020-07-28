package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.domain.course.CourseItem

sealed class MoveItemResult {
    class Success(
        val item: CourseItem,
        val itemPosition: Int
    ) : MoveItemResult()

    object MoveLimited : MoveItemResult()
}
