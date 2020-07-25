package ru.iandreyshev.light.ui.quizMaker

import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_quiz_maker_variant.view.*
import ru.iandreyshev.light.R
import ru.iandreyshev.light.utill.exhaustive
import ru.iandreyshev.light.utill.safelyPosition

class VariantAdapter(
    private val onAddNewVariant: () -> Unit
) : ListAdapter<VariantViewState, VariantViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz_maker_variant, parent, false)
            .let {
                VariantViewHolder(
                    view = it,
                    onAddNewVariant = onAddNewVariant
                )
            }

    override fun onBindViewHolder(holder: VariantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class VariantViewHolder(
    view: View,
    private val onAddNewVariant: () -> Unit
) : RecyclerView.ViewHolder(view) {

    private var mTextWatcher: TextWatcher? = null

    fun bind(viewState: VariantViewState) {
        when (viewState) {
            VariantViewState.AddNew -> {
                itemView.addNewButton.isVisible = true
                itemView.addNewButton.setOnClickListener { onAddNewVariant() }
            }
            is VariantViewState.Text -> {
                itemView.addNewButton.isVisible = false
                itemView.addNewButton.setOnClickListener(null)

                itemView.isValidCheckbox.setOnCheckedChangeListener(null)
                itemView.isValidCheckbox.isChecked = viewState.isValid
                itemView.isValidCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    viewState.onValidStateChanged(isChecked)
                }

                itemView.title.text = itemView.resources
                    .getString(R.string.quiz_maker_variant_title, viewState.position)

                mTextWatcher?.let(itemView.variantInput::removeTextChangedListener)
                itemView.variantInput.setText(viewState.text)
                mTextWatcher = itemView.variantInput.doAfterTextChanged {
                    viewState.onTextChanged(it.toString())
                }

                itemView.deleteButton.setOnClickListener {
                    viewState.onDeleteVariant()
                }
            }
        }.exhaustive
    }

}

private object DiffCallback : DiffUtil.ItemCallback<VariantViewState>() {
    override fun areItemsTheSame(oldItem: VariantViewState, newItem: VariantViewState) = false
    override fun areContentsTheSame(oldItem: VariantViewState, newItem: VariantViewState) = false
}
