package ru.iandreyshev.player.quiz

import android.view.View
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_quiz_player_question.view.*
import ru.iandreyshev.player.R

class QuestionItem(
    private val text: String,
    private val position: Int,
    private val questionsCount: Int
) : Item<QuestionItem.ViewHolder>() {

    override fun getLayout() = R.layout.item_quiz_player_question

    override fun createViewHolder(itemView: View) =
        ViewHolder(itemView)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.progressTitle.text =
            viewHolder.itemView.resources
                .getString(R.string.quiz_progress_title_in_progress, this.position, questionsCount)
        viewHolder.itemView.questionText.text = text
    }

    class ViewHolder(view: View) : GroupieViewHolder(view)

}
