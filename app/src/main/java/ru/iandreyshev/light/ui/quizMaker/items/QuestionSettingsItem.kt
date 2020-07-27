package ru.iandreyshev.light.ui.quizMaker.items

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_quiz_maker_question_settings.view.*
import ru.iandreyshev.light.R
import ru.iandreyshev.light.ui.quizMaker.QuestionSettingsViewState

class QuestionSettingsItem(
    private val viewState: QuestionSettingsViewState,
    private val onSwitchMode: () -> Unit,
    private val onDeleteQuestion: () -> Unit
) : Item<QuestionSettingsItem.ViewHolder>() {

    override fun getId() = layout.toLong()

    override fun getLayout() = R.layout.item_quiz_maker_question_settings

    override fun createViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(viewState, onSwitchMode, onDeleteQuestion)
    }

    class ViewHolder(view: View) : GroupieViewHolder(view) {

        fun bind(
            viewState: QuestionSettingsViewState,
            onSwitchMode: () -> Unit,
            onDeleteQuestion: () -> Unit
        ) {
            with(itemView) {
                multipleVariantsButton.setOnClickListener { onSwitchMode() }

                multipleVariantsSwitch.setOnCheckedChangeListener(null)
                multipleVariantsSwitch.isChecked = viewState.isMultipleMode
                multipleVariantsSwitch.setOnCheckedChangeListener { _, _ -> onSwitchMode() }

                deleteQuestionButton.isVisible = viewState.canDelete
                deleteQuestionButton.setOnClickListener { onDeleteQuestion() }
            }
        }

    }

}
