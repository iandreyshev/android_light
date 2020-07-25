package ru.iandreyshev.light.ui.quizMaker

import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import kotlinx.android.synthetic.main.fragment_quiz_maker.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.utill.withItemListeners
import ru.iandreyshev.light.utill.uiLazy

class QuizMakerFragment : BaseFragment(R.layout.fragment_quiz_maker) {

    private val mViewModel by viewModel<QuizMakerViewModel> {
        parametersOf(getScope(R.id.nav_editor))
    }
    private val mAdapter by uiLazy { VariantAdapter(mViewModel::onAddVariant) }
    private var mQuestionInputListener: TextWatcher? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.onCreate()

        initMenu()
        initQuestion()
        initVariantsList()
        initQuestionsCounter()
    }

    private fun initMenu() {
        toolbar.setNavigationOnClickListener { router().back() }
        toolbar.withItemListeners {
            R.id.actionQuizMakerSave { mViewModel.onSave() }
        }

        mViewModel.eventExit {
            router().back()
        }
    }

    private fun initQuestion() {
        mViewModel.question.viewObserveWith { question ->
            questionTitle.text = getString(R.string.quiz_maker_question_title, question.position)

            questionInput.removeTextChangedListener(mQuestionInputListener)
            questionInput.setText(question.question.text)

            mQuestionInputListener = questionInput.doAfterTextChanged { editable ->
                mViewModel.onQuestionChanged(editable.toString())
            }

            deleteQuestionButton.isVisible = question.canDelete
            deleteQuestionButton.setOnClickListener { mViewModel.onQuestionDeleted() }

            nextQuestionButton.setOnClickListener { mViewModel.onNext() }
            nextQuestionButton.setImageResource(
                when (question.hasNext) {
                    true -> R.drawable.ic_quiz_maker_next_question
                    false -> R.drawable.ic_quiz_maker_new_question
                }
            )

            previousQuestionButton.isVisible = question.hasPrevious
            previousQuestionButton.setOnClickListener { mViewModel.onPrevious() }
        }
    }

    private fun initVariantsList() {
        variantList.adapter = mAdapter
        mViewModel.variants.viewObserveWith(mAdapter::submitList)
    }

    private fun initQuestionsCounter() {
        mViewModel.counter.viewObserveWith {
            questionsCounter.text = "${it.questionNumber} / ${it.total}"
        }
    }

}
