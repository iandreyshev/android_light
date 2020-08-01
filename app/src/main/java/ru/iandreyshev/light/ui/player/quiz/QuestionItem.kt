package ru.iandreyshev.light.ui.player.quiz

import android.view.View
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_player_quiz_question.view.*
import ru.iandreyshev.light.R

class QuestionItem(
    private val text: String
) : Item<QuestionItem.ViewHolder>() {

    override fun getLayout() = R.layout.item_player_quiz_question

    override fun createViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.questionText.text = text
    }

    class ViewHolder(view: View) : GroupieViewHolder(view)

}