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

class VariantItem(
    private val viewState: VariantViewState,
    private val onAddNewVariant: () -> Unit = {}
) : Item<VariantItem.VariantViewHolder>() {

    override fun getLayout(): Int = R.layout.item_quiz_maker_variant

    override fun createViewHolder(itemView: View) =
        VariantViewHolder(
            itemView,
            onAddNewVariant
        )

    override fun bind(viewHolder: VariantViewHolder, position: Int) {
        viewHolder.bind(viewState)
    }

    class VariantViewHolder(
        view: View,
        private val onAddNewVariant: () -> Unit
    ) : GroupieViewHolder(view) {

        private var mTextWatcher: TextWatcher? = null

        fun bind(viewState: VariantViewState) {
            when (viewState) {
                VariantViewState.AddNew -> {
                    itemView.addNewButton.isVisible = true
                    itemView.addNewButton.setOnClickListener { onAddNewVariant() }

                    itemView.rootCardView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        topMargin = 0
                    }
                }
                is VariantViewState.Text -> {
                    itemView.addNewButton.isVisible = false
                    itemView.addNewButton.setOnClickListener(null)

                    itemView.isValidRadiobutton.isVisible = !viewState.isMultipleMode
                    itemView.isValidRadiobutton.setOnCheckedChangeListener(null)
                    itemView.isValidRadiobutton.isChecked = viewState.isValid
                    itemView.isValidRadiobutton.setOnCheckedChangeListener { _, isChecked ->
                        viewState.onValidStateChanged(isChecked)
                    }

                    itemView.isValidCheckbox.isVisible = viewState.isMultipleMode
                    itemView.isValidCheckbox.setOnCheckedChangeListener(null)
                    itemView.isValidCheckbox.isChecked = viewState.isValid
                    itemView.isValidCheckbox.setOnCheckedChangeListener { _, isChecked ->
                        viewState.onValidStateChanged(isChecked)
                    }

                    mTextWatcher?.let(itemView.variantInput::removeTextChangedListener)
                    itemView.variantInput.setText(viewState.text)
                    mTextWatcher = itemView.variantInput.doAfterTextChanged {
                        viewState.onTextChanged(it.toString())
                    }

                    itemView.deleteButton.setOnClickListener {
                        viewState.onDeleteVariant()
                    }

                    itemView.rootCardView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        topMargin = when (viewState.isFirstVariant) {
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
