package ru.iandreyshev.light.ui.player.quiz

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_quiz_player_variant.view.*
import ru.iandreyshev.light.R

class VariantItem(
    private val text: String,
    private val isMultipleMode: Boolean,
    private val isSelectedAsValid: Boolean,
    private val isValid: Boolean,
    private val isQuestionSubmitted: Boolean,
    private val onValidStateSwitched: () -> Unit
) : Item<VariantItem.ViewHolder>() {

    override fun getLayout() = R.layout.item_quiz_player_variant

    override fun createViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.variantText.text = text

        viewHolder.itemView.isValidCheckbox.isVisible = isMultipleMode && !isQuestionSubmitted
        viewHolder.itemView.isValidCheckbox.setOnCheckedChangeListener(null)
        viewHolder.itemView.isValidCheckbox.isChecked = isSelectedAsValid
        viewHolder.itemView.isValidCheckbox.setOnCheckedChangeListener { _, _ ->
            onValidStateSwitched()
        }

        viewHolder.itemView.isValidRadioButton.isVisible = !isMultipleMode && !isQuestionSubmitted
        viewHolder.itemView.isValidRadioButton.setOnCheckedChangeListener(null)
        viewHolder.itemView.isValidRadioButton.isChecked = isSelectedAsValid
        viewHolder.itemView.isValidRadioButton.setOnCheckedChangeListener { _, _ ->
            onValidStateSwitched()
        }
    }

    class ViewHolder(view: View) : GroupieViewHolder(view)

}
