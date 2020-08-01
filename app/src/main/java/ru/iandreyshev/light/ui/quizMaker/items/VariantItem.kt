package ru.iandreyshev.light.ui.quizMaker.items

import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_quiz_maker_variant.view.*
import ru.iandreyshev.light.R
import ru.iandreyshev.light.ui.quizMaker.VariantViewState
import ru.iandreyshev.light.utill.exhaustive
import ru.iandreyshev.light.utill.uiLazy

class VariantItem(
    private val viewState: VariantViewState,
    private val onTextChanged: (String) -> Unit = {},
    private val onValidStateSwitched: () -> Unit = {},
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

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(
            viewState,
            onTextChanged,
            onValidStateSwitched,
            onDeleteVariant,
            onAddNewVariant
        )
    }

    class ViewHolder(view: View) : GroupieViewHolder(view) {

        private var mTextWatcher: TextWatcher? = null

        fun bind(
            viewState: VariantViewState,
            onTextChanged: (String) -> Unit,
            onValidStateSwitched: () -> Unit,
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
                                .getDimensionPixelSize(R.dimen.grid_step_vertical_4)
                            else -> 0
                        }
                    }
                }
                is VariantViewState.Text -> {
                    itemView.addNewButton.isVisible = false
                    itemView.addNewButton.setOnClickListener(null)

                    itemView.isValidRadioButton.isVisible = !viewState.isMultipleMode
                    itemView.isValidRadioButton.setOnCheckedChangeListener(null)
                    itemView.isValidRadioButton.isChecked = viewState.isValid
                    itemView.isValidRadioButton.setOnCheckedChangeListener { _, _ ->
                        onValidStateSwitched()
                    }

                    itemView.isValidCheckbox.isVisible = viewState.isMultipleMode
                    itemView.isValidCheckbox.setOnCheckedChangeListener(null)
                    itemView.isValidCheckbox.isChecked = viewState.isValid
                    itemView.isValidCheckbox.setOnCheckedChangeListener { _, _ ->
                        onValidStateSwitched()
                    }

                    mTextWatcher?.let(itemView.variantText::removeTextChangedListener)
                    itemView.variantText.setText(viewState.text)
                    mTextWatcher = itemView.variantText.doAfterTextChanged {
                        onTextChanged(it.toString())
                    }

                    itemView.deleteButton.setOnClickListener {
                        onDeleteVariant()
                    }

                    itemView.rootCardView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        topMargin = when (viewState.isFirstInBlock) {
                            true -> itemView.resources
                                .getDimensionPixelSize(R.dimen.grid_step_vertical_4)
                            else -> 0
                        }
                    }
                }
            }.exhaustive
        }

    }

}
