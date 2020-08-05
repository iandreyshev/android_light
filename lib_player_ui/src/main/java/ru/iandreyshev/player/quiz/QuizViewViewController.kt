package ru.iandreyshev.player.quiz

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.lay_player_quiz_view.view.*
import ru.iandreyshev.core_utils.exhaustive
import ru.iandreyshev.core_utils.uiLazy
import ru.iandreyshev.player_core.quiz.QuizPlayerState
import ru.iandreyshev.player_core.quiz.State
import ru.iandreyshev.player_core.quiz.Wish

internal class QuizViewViewController(
    private val view: View,
    private val onWish: (Wish) -> Unit
) {

    private val mQuizAdapter by uiLazy { GroupAdapter<GroupieViewHolder>() }

    init {
        view.quizRecyclerView.adapter = mQuizAdapter
        view.submitButton.setOnClickListener { onWish(Wish.Submit) }
    }

    fun render(state: QuizPlayerState) = with(view) {
        quizStartView.isVisible = state.type == State.Type.PREVIEW
        questionView.isVisible = state.type == State.Type.QUESTION
        quizResultView.isVisible = state.type == State.Type.RESULTS

        when (state.type) {
            State.Type.DISABLED -> renderDisabled()
            State.Type.PREVIEW -> renderStart(state)
            State.Type.QUESTION -> renderQuestion(state)
            State.Type.RESULTS -> renderResult(state)
        }.exhaustive
    }

    private fun renderDisabled() = with(view) {
        isVisible = false
    }

    private fun renderStart(state: State) = with(view) {
        isVisible = true
        aboutQuiz.text = "${state.questionsCount} questions"
        startButton.setOnClickListener { onWish(Wish.Submit) }
    }

    private fun renderQuestion(state: State) = with(view) {
        isVisible = true
        toolbar.title =
            "Question (${state.questionIndex + 1} / ${state.questionsCount})"
        submitButton.text = when {
            state.questionResult == null -> "Submit"
            !state.hasNext() -> "Show results"
            else -> "Next"
        }
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
                        isSelectedAsValid = variant.isSelectedAsValid,
                        isValid = variant.isValid,
                        isQuestionSubmitted = state.questionResult != null,
                        onValidStateSwitched = { onWish(Wish.SwitchVariantValidState(index)) }
                    )
                }
            )
        })
    }

    private fun renderResult(state: State) = with(view) {
        isVisible = true
        resultText.text = state.questionResult.toString()
        finishButton.setOnClickListener { onWish(Wish.Submit) }
    }

}
