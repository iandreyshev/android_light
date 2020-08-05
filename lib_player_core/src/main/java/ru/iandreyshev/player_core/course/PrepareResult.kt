package ru.iandreyshev.player_core.course

internal sealed class PrepareResult {
    class Success(
        val item: PlayerItem,
        val itemsCount: Int
    ) : PrepareResult()

    object ErrorGettingCourse : PrepareResult()
    object ErrorCourseIsEmpty : PrepareResult()
}
