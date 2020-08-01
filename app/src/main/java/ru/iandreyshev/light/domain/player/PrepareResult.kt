package ru.iandreyshev.light.domain.player

sealed class PrepareResult {
    class Success(
        val item: PlayerCourseItem,
        val itemsCount: Int
    ) : PrepareResult()

    object ErrorGettingCourse : PrepareResult()
    object ErrorCourseIsEmpty : PrepareResult()
}
