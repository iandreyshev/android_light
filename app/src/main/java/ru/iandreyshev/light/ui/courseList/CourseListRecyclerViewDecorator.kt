package ru.iandreyshev.light.ui.courseList

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.iandreyshev.light.R

class CourseListRecyclerViewDecorator : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val resources = parent.resources

        if (position == RecyclerView.NO_POSITION) {
            return
        }

        if (position == 0) {
            outRect.top = resources.getDimensionPixelSize(R.dimen.grid_step_3)
        }

        outRect.left = resources.getDimensionPixelSize(R.dimen.grid_step_4)
        outRect.right = resources.getDimensionPixelSize(R.dimen.grid_step_4)
        outRect.bottom = resources.getDimensionPixelSize(R.dimen.grid_step_2)
    }

}