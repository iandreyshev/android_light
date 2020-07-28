package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.domain.course.Course
import ru.iandreyshev.light.domain.course.CourseItem

class CoursePlayer(
    private val dataSource: IPlayerDataSource
) : ICoursePlayer {

    private lateinit var mCourse: Course
    private lateinit var mCurrentItem: CourseItem
    private var mCurrentItemPosition: Int = 0

    override fun prepare(): PrepareResult {
        mCourse = dataSource.getCourse()
            ?: return PrepareResult.ErrorGettingCourse
        mCurrentItem = mCourse.items.firstOrNull()
            ?: return PrepareResult.ErrorCourseIsEmpty

        val itemsCount = mCourse.items.count()

        return PrepareResult.Success(mCurrentItem, itemsCount)
    }

    override fun forward(): MoveItemResult {
        if (mCourse.items.lastIndex == mCurrentItemPosition) {
            return MoveItemResult.MoveLimited
        }

        ++mCurrentItemPosition
        mCurrentItem = mCourse.items[mCurrentItemPosition]

        return MoveItemResult.Success(
            item = mCurrentItem,
            itemPosition = mCurrentItemPosition
        )
    }

    override fun back(): MoveItemResult {
        if (mCurrentItemPosition == 0) {
            return MoveItemResult.MoveLimited
        }

        --mCurrentItemPosition
        mCurrentItem = mCourse.items[mCurrentItemPosition]

        return MoveItemResult.Success(
            item = mCurrentItem,
            itemPosition = mCurrentItemPosition
        )
    }

}
