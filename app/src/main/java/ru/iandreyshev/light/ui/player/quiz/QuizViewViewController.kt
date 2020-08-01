package ru.iandreyshev.light.ui.player.quiz

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.lay_quiz_view.view.*
import ru.iandreyshev.light.ui.player.CourseItemState
import ru.iandreyshev.light.ui.player.quiz.mvi.QuizPlayerFeature
import ru.iandreyshev.light.ui.player.quiz.mvi.State
import ru.iandreyshev.light.ui.player.quiz.mvi.Wish
import ru.iandreyshev.light.utill.exhaustive
import ru.iandreyshev.light.utill.uiLazy

class QuizViewViewController(
    private val view: View
) {

    private val mQuizAdapter by uiLazy { GroupAdapter<GroupieViewHolder>() }
    private var mPlayerFeature: QuizPlayerFeature? = null
    private var mPlayerDisposable = Disposables.empty()

    init {
        view.quizRecyclerView.adapter = mQuizAdapter
        view.submitButton.setOnClickListener {
            mPlayerFeature?.accept(Wish.Submit)
        }
    }

    fun update(state: CourseItemState.Quiz) {
        mPlayerDisposable.dispose()
        mPlayerDisposable = Observable.wrap(state.feature)
            .subscribe(::render)
        mPlayerFeature = state.feature
    }

    fun hide() {
        dispose()
        view.isVisible = false
    }

    fun dispose() {
        mPlayerDisposable.dispose()
        mPlayerFeature = null
    }

    private fun render(state: State) = with(view) {
        isVisible = true
        quizStartView.isVisible = state.type == State.Type.START
        questionView.isVisible = state.type == State.Type.QUESTION
        quizResultView.isVisible = state.type == State.Type.RESULTS

        when (state.type) {
            State.Type.START -> renderStart(state)
            State.Type.QUESTION -> renderQuestion(state)
            State.Type.RESULTS -> renderResult(state)
        }.exhaustive
    }

    private fun renderStart(state: State) = with(view) {
        aboutQuiz.text = "${state.questionsCount} questions"
        startButton.setOnClickListener {
            mPlayerFeature?.accept(Wish.Submit)
        }
    }

    private fun renderQuestion(state: State) = with(view) {
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
                        onValidStateSwitched = {
                            mPlayerFeature?.accept(Wish.SwitchVariantValidState(index))
                        }
                    )
                }
            )
        })
    }

    private fun renderResult(state: State) = with(view) {
        resultText.text = state.questionResult.toString()
        finishButton.setOnClickListener {
            mPlayerFeature?.accept(Wish.Submit)
        }
    }

}
