package ru.iandreyshev.light.ui.editor

import android.view.View
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_timeline_quiz.view.*
import ru.iandreyshev.light.R

class QuizItem(
    private val id: Long,
    private val questionsCount: Int,
    private val onClickListener: () -> Unit
) : Item<QuizItem.ViewHolder>() {

    override fun getId() = id

    override fun getLayout() = R.layout.item_timeline_quiz

    override fun createViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.clickableArea.setOnClickListener { onClickListener() }
        viewHolder.itemView.subtitle.text = "$questionsCount questions"
    }

    class ViewHolder(view: View) : GroupieViewHolder(view)

}
