package ru.iandreyshev.light.domain.player

sealed class PrepareResult {
    class Success(
        val item: PlayerItem,
        val itemsCount: Int
    ) : PrepareResult()

    object ErrorGettingCourse : PrepareResult()
    object ErrorCourseIsEmpty : PrepareResult()
}
