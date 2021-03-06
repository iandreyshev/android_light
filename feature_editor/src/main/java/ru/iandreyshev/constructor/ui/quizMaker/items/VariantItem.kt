package ru.iandreyshev.constructor.ui.quizMaker.items

import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_quiz_maker_variant.view.*
import ru.iandreyshev.constructor.R
import ru.iandreyshev.constructor.ui.quizMaker.VariantViewState
import ru.iandreyshev.core_utils.exhaustive
import ru.iandreyshev.core_utils.uiLazy

class VariantItem(
    private val viewState: VariantViewState,
    private val onTextChanged: (String) -> Unit = {},
    private val onCorrectStateToggle: () -> Unit = {},
    private val onDeleteVariant: () -> Unit = {},
    private val onAddNewVariant: () -> Unit = {}
) : Item<VariantItem.ViewHolder>() {

    private val mId: Long by uiLazy {
        when (viewState) {
            is VariantViewState.NewVariantButton -> ItemIds.ADD_NEW_VARIANT_BUTTON
            is VariantViewState.Text -> ItemIds.variantIdFrom(viewState.position)
        }
    }

    override fun getId() = mId

    override fun getLayout(): Int = R.layout.item_quiz_maker_variant

    override fun createViewHolder(itemView: View) = ViewHolder(itemView)

    override fun hasSameContentAs(other: Item<*>): Boolean =
        other is VariantItem && other.viewState == viewState

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(
            viewState,
            onTextChanged,
            onCorrectStateToggle,
            onDeleteVariant,
            onAddNewVariant
        )
    }

    class ViewHolder(view: View) : GroupieViewHolder(view) {

        private var mTextWatcher: TextWatcher? = null

        fun bind(
            viewState: VariantViewState,
            onTextChanged: (String) -> Unit,
            onCorrectStateToggle: () -> Unit,
            onDeleteVariant: () -> Unit,
            onAddNewVariant: () -> Unit
        ) {
            when (viewState) {
                is VariantViewState.NewVariantButton -> {
                    itemView.addNewButton.isVisible = true
                    itemView.addNewButton.setOnClickListener { onAddNewVariant() }

                    itemView.rootCardView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        topMargin = when (viewState.isFirstInBlock) {
                            true -> itemView.resources
                                .getDimensionPixelSize(R.dimen.grid_step_4)
                            else -> 0
                        }
                    }
                }
                is VariantViewState.Text -> {
                    itemView.addNewButton.isVisible = false
                    itemView.addNewButton.setOnClickListener(null)

                    itemView.isCorrectRadioButton.isVisible = !viewState.isMultipleMode
                    itemView.isCorrectRadioButton.setOnCheckedChangeListener(null)
                    itemView.isCorrectRadioButton.isChecked = viewState.isCorrect
                    itemView.isCorrectRadioButton.setOnCheckedChangeListener { _, _ ->
                        onCorrectStateToggle()
                    }

                    itemView.isCorrectCheckbox.isVisible = viewState.isMultipleMode
                    itemView.isCorrectCheckbox.setOnCheckedChangeListener(null)
                    itemView.isCorrectCheckbox.isChecked = viewState.isCorrect
                    itemView.isCorrectCheckbox.setOnCheckedChangeListener { _, _ ->
                        onCorrectStateToggle()
                    }

                    mTextWatcher?.let(itemView.variantText::removeTextChangedListener)
                    itemView.variantText.setText(viewState.text)
                    mTextWatcher = itemView.variantText.doAfterTextChanged {
                        onTextChanged(it.toString())
                    }

                    itemView.deleteButton.isVisible = viewState.canDelete
                    itemView.deleteButton.setOnClickListener {
                        onDeleteVariant()
                    }

                    itemView.rootCardView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        topMargin = when (viewState.isFirstInBlock) {
                            true -> itemView.resources
                                .getDimensionPixelSize(R.dimen.grid_step_4)
                            else -> 0
                        }
                    }
                }
            }.exhaustive
        }

    }

}
