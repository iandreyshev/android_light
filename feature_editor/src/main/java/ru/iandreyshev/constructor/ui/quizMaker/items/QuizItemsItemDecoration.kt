package ru.iandreyshev.constructor.ui.quizMaker.items

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.iandreyshev.constructor.R

class QuizItemsItemDecoration : RecyclerView.ItemDecoration() {

    var lastItemBottomMargin = 0

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        if (position == RecyclerView.NO_POSITION) {
            return
        }

        if (position + 1 == parent.adapter?.itemCount) {
            outRect.bottom = lastItemBottomMargin +
                    parent.resources.getDimensionPixelSize(R.dimen.grid_step_6)
        }
    }

}