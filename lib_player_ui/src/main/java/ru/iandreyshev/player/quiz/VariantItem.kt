package ru.iandreyshev.player.quiz

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_quiz_player_variant.view.*
import ru.iandreyshev.player.R

class VariantItem(
    private val text: String,
    private val isMultipleMode: Boolean,
    private val isSelectedAsCorrect: Boolean,
    private val isCorrect: Boolean,
    private val isQuestionSubmitted: Boolean,
    private val onCorrectStateToggle: () -> Unit
) : Item<VariantItem.ViewHolder>() {

    override fun getLayout() = R.layout.item_quiz_player_variant

    override fun createViewHolder(itemView: View) =
        ViewHolder(itemView)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.variantText.text = text

        viewHolder.itemView.isCorrectCheckbox.isVisible = isMultipleMode && !isQuestionSubmitted
        viewHolder.itemView.isCorrectCheckbox.setOnCheckedChangeListener(null)
        viewHolder.itemView.isCorrectCheckbox.isChecked = isSelectedAsCorrect
        viewHolder.itemView.isCorrectCheckbox.setOnCheckedChangeListener { _, _ ->
            onCorrectStateToggle()
        }

        viewHolder.itemView.isCorrectRadioButton.isVisible = !isMultipleMode && !isQuestionSubmitted
        viewHolder.itemView.isCorrectRadioButton.setOnCheckedChangeListener(null)
        viewHolder.itemView.isCorrectRadioButton.isChecked = isSelectedAsCorrect
        viewHolder.itemView.isCorrectRadioButton.setOnCheckedChangeListener { _, _ ->
            onCorrectStateToggle()
        }
    }

    class ViewHolder(view: View) : GroupieViewHolder(view)

}
