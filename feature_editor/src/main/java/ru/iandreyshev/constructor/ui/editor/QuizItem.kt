package ru.iandreyshev.constructor.ui.editor

import android.view.View
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_timeline_quiz.view.*
import ru.iandreyshev.constructor.R

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
        viewHolder.itemView.title.text = "Название теста"
        viewHolder.itemView.subtitle.text =
            viewHolder.itemView.resources.getString(R.string.editor_quiz_subtitle, questionsCount)
    }

    class ViewHolder(view: View) : GroupieViewHolder(view)

}
