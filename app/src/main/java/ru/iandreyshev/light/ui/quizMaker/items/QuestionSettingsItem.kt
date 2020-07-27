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
    private val onChangeMode: (Boolean) -> Unit,
    private val onDeleteQuestion: () -> Unit
) : Item<QuestionSettingsItem.ViewHolder>() {

    override fun getLayout() = R.layout.item_quiz_maker_question_settings

    override fun createViewHolder(itemView: View) =
        ViewHolder(itemView, onChangeMode, onDeleteQuestion)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(viewState)
    }

    class ViewHolder(
        view: View,
        private val onChangeMode: (Boolean) -> Unit,
        private val onDeleteQuestion: () -> Unit
    ) : GroupieViewHolder(view) {

        fun bind(viewState: QuestionSettingsViewState) {
            with(itemView) {
                hasMultipleVariantsSwitch.onFocusChangeListener = null
                hasMultipleVariantsSwitch.isChecked = viewState.isMultipleMode
                hasMultipleVariantsSwitch.setOnFocusChangeListener { v, hasFocus ->
                    onChangeMode(hasFocus)
                }

                deleteQuestionButton.isVisible = viewState.canDelete
                deleteQuestionButton.setOnClickListener { onDeleteQuestion() }
            }
        }

    }

}
