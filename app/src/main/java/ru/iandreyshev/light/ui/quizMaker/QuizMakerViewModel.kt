package ru.iandreyshev.light.ui.quizMaker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.quizMaker.IQuizMakerRepository
import ru.iandreyshev.light.domain.quizMaker.ISaveQuizDraftUseCase
import ru.iandreyshev.light.domain.quizMaker.Quiz
import ru.iandreyshev.light.domain.quizMaker.draft.QuizDraft
import ru.iandreyshev.light.ui.quizMaker.items.QuestionItem
import ru.iandreyshev.light.ui.quizMaker.items.QuestionSettingsItem
import ru.iandreyshev.light.ui.quizMaker.items.VariantItem
import ru.iandreyshev.light.utill.*

class QuizMakerViewModel(scope: Scope) : ViewModel() {

    private val mRepository by uiLazy { scope.get<IQuizMakerRepository>() }
    private val mSaveDraft by uiLazy { scope.get<ISaveQuizDraftUseCase>() }

    val adapter = GroupAdapter<GroupieViewHolder>()

    val eventExit = voidSingleLiveEvent()
    val eventShowError = singleLiveEvent<String>()

    private lateinit var mDraft: QuizDraft

    fun onCreate() {
        viewModelScope.launch {
            val quiz = mRepository.get()
            mDraft = buildQuizViewState(quiz)
            updateDraftView()
        }
    }

    fun onQuestionChanged(text: String) {
        mDraft.updateQuestionText(mDraft.currentQuestionPosition, text)
    }

    fun onDeleteQuestion() {
        mDraft.deleteQuestionAt(mDraft.currentQuestionPosition)
        updateDraftView()
    }

    fun onAddVariant() {
        mDraft.addVariant(mDraft.currentQuestionPosition)
        updateDraftView()
    }

    fun onNext() {
        if (mDraft.hasNext) {
            mDraft.nextQuestion()
        } else {
            mDraft.addQuestion()
            mDraft.addVariant(mDraft.currentQuestionPosition)
            mDraft.nextQuestion()
        }
        updateDraftView()
    }

    fun onPrevious() {
        mDraft.previousQuestion()
        updateDraftView()
    }

    fun onSave() {
        viewModelScope.launch {
            mSaveDraft(mDraft)
            eventExit()
        }
    }

    private fun buildQuizViewState(quiz: Quiz?): QuizDraft {
        quiz ?: return QuizDraft().apply {
            addQuestion()
            addVariant(currentQuestionPosition)
        }

        return QuizDraft(quiz)
    }

    private fun updateDraftView() {
        val currQuestion = mDraft.currentQuestion
        val currQuestionPosition = mDraft.currentQuestionPosition

        adapter.update(mutableListOf<Item<*>>().apply {
            add(
                QuestionItem(
                    viewState = CurrentQuestionViewState(
                        hasPrevious = mDraft.hasPrevious,
                        hasNext = mDraft.hasNext,
                        question = QuestionViewState(
                            id = currQuestion.id,
                            text = currQuestion.text
                        ),
                        position = currQuestionPosition + 1,
                        counter = CounterViewState(
                            questionNumber = mDraft.currentQuestionPosition + 1,
                            total = mDraft.questionsCount
                        )
                    ),
                    onQuestionChanged = ::onQuestionChanged,
                    onPreviousQuestion = ::onPrevious,
                    onNextQuestion = ::onNext
                )
            )
            addAll(
                currQuestion.variants.mapIndexed { pos, draft ->
                    VariantItem(
                        viewState = VariantViewState.Text(
                            id = draft.id,
                            text = draft.text,
                            isFirstVariant = pos == 0,
                            isValid = draft.isValid,
                            isMultipleMode = true,
                            onTextChanged = { text ->
                                mDraft.updateVariant(currQuestionPosition, pos, text)
                            },
                            onValidStateChanged = { isValid ->
                                mDraft.updateVariant(
                                    currQuestionPosition,
                                    pos,
                                    isValid = isValid
                                )
                            },
                            onDeleteVariant = {
                                mDraft.deleteVariantAt(currQuestionPosition, pos)
                                updateDraftView()
                            }
                        )
                    )
                }
            )
            add(
                VariantItem(
                    viewState = VariantViewState.AddNew,
                    onAddNewVariant = ::onAddVariant
                )
            )
            add(
                QuestionSettingsItem(
                    viewState = QuestionSettingsViewState(
                        isMultipleMode = true,
                        canDelete = mDraft.questionsCount > 1
                    ),
                    onChangeMode = {},
                    onDeleteQuestion = ::onDeleteQuestion
                )
            )
        })
    }

}
