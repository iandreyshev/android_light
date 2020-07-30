package ru.iandreyshev.light.domain.player

sealed class PrepareResult {
    class Success(
        val item: ItemState,
        val itemsCount: Int
    ) : PrepareResult()

    object ErrorGettingCourse : PrepareResult()
    object ErrorCourseIsEmpty : PrepareResult()
}
