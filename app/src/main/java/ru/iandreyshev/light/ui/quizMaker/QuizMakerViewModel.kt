package ru.iandreyshev.light.ui.quizMaker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.quizMaker.IQuizMakerRepository
import ru.iandreyshev.light.domain.quizMaker.ISaveQuizDraftUseCase
import ru.iandreyshev.light.domain.quizMaker.Quiz
import ru.iandreyshev.light.domain.quizMaker.draft.QuizDraft
import ru.iandreyshev.light.domain.quizMaker.draft.VariantDraft
import ru.iandreyshev.light.utill.*

class QuizMakerViewModel(scope: Scope) : ViewModel() {

    private val mRepository by uiLazy { scope.get<IQuizMakerRepository>() }
    private val mSaveDraft by uiLazy { scope.get<ISaveQuizDraftUseCase>() }

    val question by uiLazy { mQuestion.distinctUntilChanged() }
    val variants by uiLazy { mVariants.distinctUntilChanged() }
    val counter by uiLazy { mCounter.distinctUntilChanged() }

    private val mQuestion = MutableLiveData<CurrentQuestionViewState>()
    private val mVariants = MutableLiveData<List<VariantViewState>>()
    private val mCounter = MutableLiveData(CounterViewState(0, 0))

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

    fun onQuestionDeleted() {
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
        }

        return QuizDraft(quiz)
    }

    private fun updateDraftView() {
        val currQuestion = mDraft.currentQuestion
        val currQuestionPosition = mDraft.currentQuestionPosition

        mQuestion.value = CurrentQuestionViewState(
            hasPrevious = mDraft.hasPrevious,
            hasNext = mDraft.hasNext,
            question = QuestionViewState(
                id = currQuestion.id,
                text = currQuestion.text
            ),
            position = currQuestionPosition + 1,
            canDelete = mDraft.questionsCount > 1
        )
        mVariants.value = currQuestion.variants
            .mapIndexed<VariantDraft, VariantViewState> { pos, draft ->
                VariantViewState.Text(
                    id = draft.id,
                    text = draft.text,
                    isValid = draft.isValid,
                    position = pos + 1,
                    onTextChanged = { text ->
                        mDraft.updateVariant(currQuestionPosition, pos, text)
                    },
                    onValidStateChanged = { isValid ->
                        mDraft.updateVariant(currQuestionPosition, pos, isValid = isValid)
                    },
                    onDeleteVariant = {
                        mDraft.deleteVariantAt(currQuestionPosition, pos)
                        updateDraftView()
                    }
                )
            }
            .toMutableList()
            .apply { add(VariantViewState.AddNew) }
        mCounter.value = CounterViewState(
            questionNumber = mDraft.currentQuestionPosition + 1,
            total = mDraft.questionsCount
        )
    }

}
