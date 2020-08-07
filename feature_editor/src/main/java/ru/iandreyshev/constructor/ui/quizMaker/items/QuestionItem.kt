package ru.iandreyshev.constructor.ui.quizMaker.items

import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_quiz_maker_question.view.*
import ru.iandreyshev.constructor.R
import ru.iandreyshev.constructor.ui.quizMaker.CurrentQuestionViewState

class QuestionItem(
    private val viewState: CurrentQuestionViewState,
    private val onQuestionChanged: (String) -> Unit,
    private val onPreviousQuestion: () -> Unit,
    private val onNextQuestion: () -> Unit
) : Item<QuestionItem.ViewHolder>() {

    override fun getId() = layout.toLong()

    override fun getLayout() = R.layout.item_quiz_maker_question

    override fun createViewHolder(itemView: View) = ViewHolder(itemView)

    override fun hasSameContentAs(other: Item<*>): Boolean =
        other is QuestionItem && other.viewState == viewState

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(
            viewState,
            onQuestionChanged,
            onPreviousQuestion,
            onNextQuestion
        )
    }

    class ViewHolder(view: View) : GroupieViewHolder(view) {

        private var mQuestionInputListener: TextWatcher? = null

        fun bind(
            viewState: CurrentQuestionViewState,
            onQuestionChanged: (String) -> Unit,
            onPreviousQuestion: () -> Unit,
            onNextQuestion: () -> Unit
        ) {
            with(itemView) {
                questionInput.removeTextChangedListener(mQuestionInputListener)
                questionInput.setText(viewState.question.text)

                questionsCounter.text =
                    "${viewState.counter.questionNumber} / ${viewState.counter.total}"

                mQuestionInputListener = questionInput.doAfterTextChanged { editable ->
                    onQuestionChanged(editable.toString())
                }

                nextQuestionButton.setOnClickListener { onNextQuestion() }
                nextQuestionButton.setImageResource(
                    when (viewState.hasNext) {
                        true -> R.drawable.ic_quiz_maker_next_question
                        false -> R.drawable.ic_quiz_maker_new_question
                    }
                )

                previousQuestionButton.isVisible = viewState.hasPrevious
                previousQuestionButton.setOnClickListener { onPreviousQuestion() }
            }
        }

    }

}