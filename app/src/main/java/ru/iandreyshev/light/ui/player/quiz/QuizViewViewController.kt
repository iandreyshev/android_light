package ru.iandreyshev.light.ui.player.quiz

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.lay_quiz_view.view.*
import ru.iandreyshev.light.ui.player.CourseItemState
import ru.iandreyshev.light.ui.player.quiz.mvi.State
import ru.iandreyshev.light.utill.uiLazy

class QuizViewViewController(
    private val view: View
) : Consumer<State> {

    private val mQuizAdapter by uiLazy { GroupAdapter<GroupieViewHolder>() }
    private var mPlayerDisposable = Disposables.empty()

    init {
        view.quizRecyclerView.adapter = mQuizAdapter
    }

    override fun accept(state: State) {
        view.quizView.isVisible = true
        view.toolbar.title = state.question
        mQuizAdapter.update(state.variants)
    }

    fun update(state: CourseItemState.Quiz) {
        mPlayerDisposable.dispose()
        mPlayerDisposable = Observable.wrap(state.feature)
            .subscribe(this)
    }

    fun hide() {
        dispose()
        view.isVisible = false
    }

    fun dispose() {
        mPlayerDisposable.dispose()
    }

}
