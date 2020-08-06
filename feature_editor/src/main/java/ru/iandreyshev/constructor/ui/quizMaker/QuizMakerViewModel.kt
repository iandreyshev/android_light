package ru.iandreyshev.constructor.ui.quizMaker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.constructor.domain.quiz.IQuizDraftRepository
import ru.iandreyshev.constructor.domain.quiz.draft.QuizDraft
import ru.iandreyshev.constructor.ui.quizMaker.items.QuestionItem
import ru.iandreyshev.constructor.ui.quizMaker.items.QuestionSettingsItem
import ru.iandreyshev.constructor.ui.quizMaker.items.VariantItem
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.core_ui.singleLiveEvent
import ru.iandreyshev.core_ui.voidSingleLiveEvent
import ru.iandreyshev.core_utils.uiLazy

class QuizMakerViewModel(
    scope: Scope,
    private val args: QuizMakerArgs
) : ViewModel() {

    val adapter = GroupAdapter<GroupieViewHolder>()

    val eventExit = voidSingleLiveEvent()
    val eventShowError = singleLiveEvent<String>()

    private val mRepository by uiLazy {
        scope.get<IQuizDraftRepository> {
            parametersOf(args)
        }
    }
    private lateinit var mDraft: QuizDraft

    fun onCreate() {
        viewModelScope.launch {
            mDraft = buildQuizViewState()
            updateDraftView()
        }
    }

    fun onQuestionChanged(text: String) {
        mDraft.setQuestionText(mDraft.currentQuestionPosition, text)
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
            mDraft.moveToNextQuestion()
        } else {
            mDraft.addQuestion()
            mDraft.moveToNextQuestion()
            mDraft.addVariant(mDraft.currentQuestionPosition)
        }
        updateDraftView()
    }

    fun onPrevious() {
        mDraft.moveToPreviousQuestion()
        updateDraftView()
    }

    fun onSave() {
        viewModelScope.launch {
            mRepository.save(mDraft)
            eventExit()
        }
    }

    private fun onSwitchMode() {
        mDraft.switchQuestionMultipleMode(mDraft.currentQuestionPosition)
        updateDraftView()
    }

    private fun buildQuizViewState(): QuizDraft {
        return QuizDraft(args.quizDraftId).apply {
            addQuestion()
            addVariant(currentQuestionPosition)
        }
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
                currQuestion.variants.mapIndexed { vPos, draft ->
                    VariantItem(
                        viewState = VariantViewState.Text(
                            id = draft.id,
                            text = draft.text,
                            position = vPos,
                            isFirstInBlock = vPos == 0,
                            isValid = draft.isValid,
                            isMultipleMode = currQuestion.isMultipleMode
                        ),
                        onTextChanged = { text ->
                            mDraft.setVariantText(currQuestionPosition, vPos, text)
                        },
                        onValidStateSwitched = {
                            mDraft.switchVariantValidState(currQuestionPosition, vPos)
                            updateDraftView()
                        },
                        onDeleteVariant = {
                            mDraft.deleteVariantAt(currQuestionPosition, vPos)
                            updateDraftView()
                        }
                    )
                }
            )
            add(
                VariantItem(
                    viewState = VariantViewState.NewVariantButton(
                        isFirstInBlock = currQuestion.variants.isEmpty()
                    ),
                    onAddNewVariant = ::onAddVariant
                )
            )
            add(
                QuestionSettingsItem(
                    viewState = QuestionSettingsViewState(
                        isMultipleMode = currQuestion.isMultipleMode,
                        canDelete = mDraft.questionsCount > 1
                    ),
                    onSwitchMode = ::onSwitchMode,
                    onDeleteQuestion = ::onDeleteQuestion
                )
            )
        })
    }

}
