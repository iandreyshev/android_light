package ru.iandreyshev.light.domain.player

import io.reactivex.Observable
import ru.iandreyshev.light.domain.course.Course
import ru.iandreyshev.light.domain.course.CourseItem

    class Player(
    private val dataSource: IPlayerDataSource
) : IPlayer {

    private var mCourse: Course? = null
    private var mCurrentItem: CourseItem? = null

    override fun prepare(): Observable<LoadResult> {
        mCourse = dataSource.getCourse()
        mCurrentItem = mCourse?.items?.firstOrNull()

        return Observable.just(
            when (val item = mCurrentItem) {
                null -> LoadResult.UnknownError
                else -> {
                    val itemsCount = mCourse?.items?.count() ?: 0
                    LoadResult.Success(item, itemsCount)
                }
            }
        )
    }

    override fun playWhenReady() {
        TODO("Not yet implemented")
    }

    override fun nextItem() {
        TODO("Not yet implemented")
    }

    override fun previousItem() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }

}