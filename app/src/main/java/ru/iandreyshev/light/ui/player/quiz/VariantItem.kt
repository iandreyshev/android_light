package ru.iandreyshev.light.ui.player.quiz

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_player_quiz_variant.view.*
import ru.iandreyshev.light.R

class VariantItem(
    private val text: String,
    private val isMultipleMode: Boolean,
    private val isChecked: Boolean,
    private val onCheck: (Boolean) -> Unit
) : Item<VariantItem.ViewHolder>() {

    override fun getLayout() = R.layout.item_player_quiz_variant

    override fun createViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.variantText.text = text

        viewHolder.itemView.isValidCheckbox.isVisible = !isMultipleMode
        viewHolder.itemView.isValidCheckbox.setOnCheckedChangeListener(null)
        viewHolder.itemView.isValidCheckbox.isChecked = isChecked
        viewHolder.itemView.isValidCheckbox.setOnCheckedChangeListener { _, isChecked ->
            onCheck(isChecked)
        }

        viewHolder.itemView.isValidRadioButton.isVisible = !isMultipleMode
        viewHolder.itemView.isValidRadioButton.setOnCheckedChangeListener(null)
        viewHolder.itemView.isValidRadioButton.isChecked = isChecked
        viewHolder.itemView.isValidRadioButton.setOnCheckedChangeListener { _, isChecked ->
            onCheck(isChecked)
        }
    }

    class ViewHolder(view: View) : GroupieViewHolder(view)

}
