package ru.iandreyshev.player_core.course

import ru.iandreyshev.core_utils.exhaustive

internal class CoursePlayer(
    private val dataSource: IPlayerDataSource
) : ICoursePlayer {

    private val mCourseItems = mutableListOf<PlayerItem>()
    private var mCurrentPosition: Int = 0
    private val mCurrentPlayerItem: PlayerItem
        get() = mCourseItems[mCurrentPosition]

    override fun prepare(): PrepareResult {
        val course = dataSource.getCourse()
            ?: return PrepareResult.ErrorGettingCourse

        mCourseItems.clear()
        mCourseItems.addAll(parseItems(course))

        return PrepareResult.Success(
            mCurrentPlayerItem,
            mCourseItems.count()
        )
    }

    override fun forward(): MoveItemResult {
        if (mCourseItems.lastIndex == mCurrentPosition) {
            return MoveItemResult.MoveLimited
        }

        when (val currentItem = mCurrentPlayerItem) {
            is PlayerItem.Image -> {
                mCourseItems[mCurrentPosition] = currentItem.copy(isComplete = true)
            }
            is PlayerItem.Video -> {
                mCourseItems[mCurrentPosition] = currentItem.copy(isComplete = true)
            }
            is PlayerItem.Quiz -> Unit
        }.exhaustive

        ++mCurrentPosition

        return MoveItemResult.Success(
            item = mCurrentPlayerItem,
            position = mCurrentPosition,
            itemsCount = mCourseItems.count()
        )
    }

    override fun back(): MoveItemResult {
        if (mCurrentPosition == 0) {
            return MoveItemResult.MoveLimited
        }

        --mCurrentPosition

        return MoveItemResult.Success(
            item = mCurrentPlayerItem,
            position = mCurrentPosition,
            itemsCount = mCourseItems.count()
        )
    }

    private fun parseItems(course: Course) =
        course.items
            .map { courseItem ->
                when (courseItem) {
                    is CourseItem.Quiz -> {
                        PlayerItem.Quiz(
                            quiz = courseItem,
                            result = null
                        )
                    }
                    is CourseItem.Image ->
                        PlayerItem.Image(
                            uri = courseItem.source.uri,
                            isComplete = false
                        )
                    is CourseItem.Video ->
                        PlayerItem.Video(
                            uri = courseItem.source.filePath,
                            isComplete = false
                        )
                }
            }

}
