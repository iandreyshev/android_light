package ru.iandreyshev.light.ui.player.quiz

import android.view.View
import androidx.core.view.isVisible
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.lay_quiz_view.view.*
import ru.iandreyshev.light.ui.player.CourseItemState
import ru.iandreyshev.light.ui.player.quiz.mvi.QuizPlayerFeature
import ru.iandreyshev.light.ui.player.quiz.mvi.State
import ru.iandreyshev.light.ui.player.quiz.mvi.Wish
import ru.iandreyshev.light.utill.exhaustive
import ru.iandreyshev.light.utill.uiLazy

class QuizViewViewController(
    private val view: View
) : Consumer<State> {

    private val mQuizAdapter by uiLazy { GroupAdapter<GroupieViewHolder>() }
    private var mPlayerFeature: QuizPlayerFeature? = null
    private var mPlayerDisposable = Disposables.empty()

    init {
        view.quizRecyclerView.adapter = mQuizAdapter
        view.submitButton.setOnClickListener {
            mPlayerFeature?.accept(Wish.Submit)
        }
    }

    override fun accept(state: State) {
        view.isVisible = true

        when (state.type) {
            State.Type.QUESTION -> {
                view.questionResultView.isVisible = false
                view.questionView.isVisible = true
                view.toolbar.title =
                    "Question (${state.questionIndex + 1} / ${state.questionsCount})"
                view.submitButton.text = when {
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
            State.Type.RESULTS -> {
                view.questionView.isVisible = false
                view.questionResultView.isVisible = true
                view.resultText.text = state.questionResult.toString()
                view.finishButton.setOnClickListener {
                    mPlayerFeature?.accept(Wish.Submit)
                }
            }
        }.exhaustive
    }

    fun update(state: CourseItemState.Quiz) {
        mPlayerDisposable.dispose()
        mPlayerDisposable = Observable.wrap(state.feature)
            .subscribe(this)
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

}
