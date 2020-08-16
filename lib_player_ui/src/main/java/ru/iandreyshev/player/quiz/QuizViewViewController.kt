package ru.iandreyshev.player.quiz

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.lay_player_quiz_view.view.*
import kotlinx.android.synthetic.main.lay_player_quiz_view.view.resultIcon
import ru.iandreyshev.core_utils.exhaustive
import ru.iandreyshev.core_utils.uiLazy
import ru.iandreyshev.player.R
import ru.iandreyshev.player_core.quiz.*

internal class QuizViewViewController(
    private val view: View,
    private val onWish: (Wish) -> Unit
) {

    private val mQuizAdapter by uiLazy { GroupAdapter<GroupieViewHolder>() }

    init {
        view.quizRecyclerView.adapter = mQuizAdapter
        view.submitButton.setOnClickListener { onWish(Wish.Submit) }
    }

    fun render(state: QuizPlayerState) {
        view.isVisible = state.type != State.Type.DISABLED
        view.quizResultView.isVisible = state.type == State.Type.RESULTS
        view.contentView.isVisible = state.type != State.Type.RESULTS
        view.questionView.isVisible = state.type == State.Type.QUESTION
        view.previewView.isVisible = state.type == State.Type.PREVIEW

        when (state.type) {
            State.Type.DISABLED -> Unit
            State.Type.PREVIEW -> renderPreview(state)
            State.Type.QUESTION -> renderQuestion(state)
            State.Type.RESULTS -> renderResult(state)
        }.exhaustive
    }

    fun setQuizViewTopMargin(margin: Int) {
        view.contentView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = margin
        }
    }

    private fun renderPreview(state: State) = with(view) {
        quizTitle.text = state.quizTitle
        quizSubtitle.text = resources.getString(R.string.quiz_preview_title, state.questionsCount)
        progressTitle.text = resources.getString(R.string.quiz_progress_title_not_started)
        startButton.setOnClickListener {
            onWish(Wish.Submit)
        }
    }

    private fun renderQuestion(state: State) = with(view) {
        progressProgressBar.max = state.questionsCount
        progressProgressBar.progress = when (state.questionResult) {
            QuestionResult.UNDEFINED -> state.questionIndex
            else -> state.questionIndex + 1
        }
        progressTitle.text = resources.getString(
            R.string.quiz_progress_title_in_progress,
            state.questionIndex + 1,
            state.questionsCount
        )
        submitButton.setText(
            when (state.questionResult) {
                QuestionResult.UNDEFINED -> R.string.quiz_question_submit_button
                else -> R.string.quiz_question_next_button
            }
        )
        mQuizAdapter.update(mutableListOf<Item<*>>().apply {
            add(
                QuestionItem(
                    text = state.questionText
                )
            )
            addAll(
                state.variants.mapIndexed { index, variant ->
                    VariantItem(
                        text = variant.text,
                        isMultipleMode = state.isMultipleMode,
                        isSelectedAsCorrect = variant.isSelectedAsCorrect,
                        isCorrect = variant.isCorrect,
                        isQuestionSubmitted = state.questionResult != QuestionResult.UNDEFINED,
                        onCorrectStateToggle = { onWish(Wish.SwitchVariantCorrectState(index)) }
                    )
                }
            )
        })
    }

    private fun renderResult(state: State) = with(view) {
        val resultPackage = getResultPackage(state.quizResult)
        resultTitle.setText(resultPackage.titleRes)
        resultTitle.setTextColor(ContextCompat.getColor(context, resultPackage.textColorRes))
        resultSubtitle.setText(resultPackage.subtitleRes)
        resultSubtitle.setTextColor(ContextCompat.getColor(context, resultPackage.textColorRes))
        resultIcon.setBackgroundResource(resultPackage.iconRes)
        prevButton.setOnClickListener { onWish(Wish.Back) }
        nextButton.setOnClickListener { onWish(Wish.Submit) }
    }

    private fun getResultPackage(result: QuizResult) = when (result) {
        QuizResult.SUCCESS -> ResultViewPackage(
            titleRes = R.string.quiz_result_success_title,
            subtitleRes = R.string.quiz_result_success_subtitle,
            textColorRes = R.color.green,
            iconRes = R.drawable.ic_quiz_result_success
        )
        QuizResult.PARTLY_SUCCESS -> ResultViewPackage(
            titleRes = R.string.quiz_result_partly_success_title,
            subtitleRes = R.string.quiz_result_partly_success_subtitle,
            textColorRes = R.color.yellow,
            iconRes = R.drawable.ic_quiz_result_partly_success
        )
        QuizResult.FAILURE,
        QuizResult.UNDEFINED -> ResultViewPackage(
            titleRes = R.string.quiz_result_failure_title,
            subtitleRes = R.string.quiz_result_failure_subtitle,
            textColorRes = R.color.red,
            iconRes = R.drawable.ic_quiz_result_failure
        )
    }

    private data class ResultViewPackage(
        val titleRes: Int,
        val subtitleRes: Int,
        val textColorRes: Int,
        val iconRes: Int
    )

}
