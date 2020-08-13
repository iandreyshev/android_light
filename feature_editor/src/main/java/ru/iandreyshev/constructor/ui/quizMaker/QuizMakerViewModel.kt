package ru.iandreyshev.constructor.ui.quizMaker

import androidx.lifecycle.viewModelScope
import com.xwray.groupie.Item
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.constructor.domain.quiz.IQuizDraftRepository
import ru.iandreyshev.constructor.domain.quiz.quizMaker.CreateResult
import ru.iandreyshev.constructor.domain.quiz.quizMaker.QuizMaker
import ru.iandreyshev.constructor.ui.quizMaker.items.QuestionItem
import ru.iandreyshev.constructor.ui.quizMaker.items.SettingsItem
import ru.iandreyshev.constructor.ui.quizMaker.items.VariantItem
import ru.iandreyshev.core_app.UnifiedStateViewModel
import ru.iandreyshev.core_utils.uiLazy

class QuizMakerViewModel(
    scope: Scope,
    private val args: QuizMakerArgs
) : UnifiedStateViewModel<QuizMakerViewState, QuizMakerEvent>(
    initialState = QuizMakerViewState(items = arrayListOf())
) {

    private val mRepository by uiLazy {
        scope.get<IQuizDraftRepository> { parametersOf(args) }
    }
    private lateinit var mQuizMaker: QuizMaker

    fun onCreate() {
        viewModelScope.launch {
            val draft = mRepository.get()
            mQuizMaker = QuizMaker(
                draft
            )
                .apply {
                    addQuestion()
                    addVariant()
                    addVariant()
                }
            updateDraftView()
        }
    }

    fun onSave() {
        viewModelScope.launch {
            when (val result = mQuizMaker.createDraft()) {
                is CreateResult.Success -> {
                    mRepository.save(result.draft)
                    event { QuizMakerEvent.Exit }
                }
                is CreateResult.ErrorInvalidQuestion ->
                    event {
                        val text = "Invalid question at position: ${result.position.inc()}"
                        QuizMakerEvent.ShowError(text)
                    }
            }
        }
    }

    private fun onQuestionChanged(text: String) {
        mQuizMaker.setQuestionText(text)
    }

    private fun onDeleteQuestion() {
        mQuizMaker.deleteQuestion()
        updateDraftView()
    }

    private fun onAddVariant() {
        mQuizMaker.addVariant()
        updateDraftView()
    }

    private fun onNext() {
        if (mQuizMaker.hasNext) {
            mQuizMaker.moveToNextQuestion()
        } else {
            mQuizMaker.addQuestion()
            mQuizMaker.moveToNextQuestion()
            mQuizMaker.addVariant()
            mQuizMaker.addVariant()
        }
        updateDraftView()
    }

    private fun onPrevious() {
        mQuizMaker.moveToPreviousQuestion()
        updateDraftView()
    }

    private fun onSwitchMode() {
        mQuizMaker.switchQuestionMultipleMode()
        updateDraftView()
    }

    private fun updateDraftView() {
        val currQuestion = mQuizMaker.currentQuestion
        val currQuestionPosition = mQuizMaker.currentQuestionPosition

        modifyState {
            copy(
                items = arrayListOf<Item<*>>()
                    .apply {
                        add(
                            QuestionItem(
                                viewState = CurrentQuestionViewState(
                                    hasPrevious = mQuizMaker.hasPrevious,
                                    hasNext = mQuizMaker.hasNext,
                                    question = QuestionViewState(
                                        id = currQuestion.id,
                                        text = currQuestion.text
                                    ),
                                    position = currQuestionPosition + 1,
                                    counter = CounterViewState(
                                        questionNumber = mQuizMaker.currentQuestionPosition + 1,
                                        total = mQuizMaker.questionsCount
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
                                        isCorrect = draft.isCorrect,
                                        isMultipleMode = currQuestion.isMultipleMode,
                                        canDelete = currQuestion.variants.count()
                                                > QuizMaker.MIN_VARIANTS_COUNT
                                    ),
                                    onTextChanged = { text ->
                                        mQuizMaker.setVariantText(currQuestionPosition, vPos, text)
                                    },
                                    onCorrectStateToggle = {
                                        mQuizMaker.switchVariantCorrectState(
                                            currQuestionPosition,
                                            vPos
                                        )
                                        updateDraftView()
                                    },
                                    onDeleteVariant = {
                                        mQuizMaker.deleteVariantAt(currQuestionPosition, vPos)
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
                            SettingsItem(
                                viewState = QuestionSettingsViewState(
                                    isMultipleMode = currQuestion.isMultipleMode,
                                    canDelete = mQuizMaker.questionsCount > 1
                                ),
                                onSwitchMode = ::onSwitchMode,
                                onDeleteQuestion = ::onDeleteQuestion
                            )
                        )
                    })
        }
    }

}
