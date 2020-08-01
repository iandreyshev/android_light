package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.domain.course.Course
import ru.iandreyshev.light.domain.course.CourseItem
import ru.iandreyshev.light.domain.player.quiz.QuizPlayer

class CoursePlayer(
    private val dataSource: IPlayerDataSource
) : ICoursePlayer {

    private lateinit var mCourse: Course
    private lateinit var mCurrentItem: CourseItem
    private lateinit var mCurrentItemState: PlayerCourseItem
    private var mCurrentItemPosition: Int = 0

    override fun prepare(): PrepareResult {
        mCourse = dataSource.getCourse()
            ?: return PrepareResult.ErrorGettingCourse
        mCurrentItem = mCourse.items.firstOrNull()
            ?: return PrepareResult.ErrorCourseIsEmpty

        mCurrentItemState = when (val item = mCurrentItem) {
            is CourseItem.Quiz ->
                PlayerCourseItem.Quiz(QuizPlayer(item))
            is CourseItem.Image ->
                PlayerCourseItem.Image(item.source.filePath)
            is CourseItem.Video ->
                TODO()
        }

        val itemsCount = mCourse.items.count()

        return PrepareResult.Success(mCurrentItemState, itemsCount)
    }

    override fun forward(): MoveItemResult {
        if (mCourse.items.lastIndex == mCurrentItemPosition) {
            return MoveItemResult.MoveLimited
        }

        ++mCurrentItemPosition
        mCurrentItem = mCourse.items[mCurrentItemPosition]
        mCurrentItemState = when (val item = mCurrentItem) {
            is CourseItem.Quiz ->
                TODO()
            is CourseItem.Image ->
                PlayerCourseItem.Image(item.source.filePath)
            is CourseItem.Video ->
                TODO()
        }

        return MoveItemResult.Success(
            item = mCurrentItemState,
            itemPosition = mCurrentItemPosition
        )
    }

    override fun back(): MoveItemResult {
        if (mCurrentItemPosition == 0) {
            return MoveItemResult.MoveLimited
        }

        --mCurrentItemPosition
        mCurrentItem = mCourse.items[mCurrentItemPosition]
        mCurrentItemState = when (val item = mCurrentItem) {
            is CourseItem.Quiz ->
                TODO()
            is CourseItem.Image ->
                PlayerCourseItem.Image(item.source.filePath)
            is CourseItem.Video ->
                TODO()
        }

        return MoveItemResult.Success(
            item = mCurrentItemState,
            itemPosition = mCurrentItemPosition
        )
    }

}
