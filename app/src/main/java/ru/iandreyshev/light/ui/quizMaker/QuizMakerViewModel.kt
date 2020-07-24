package ru.iandreyshev.light.ui.quizMaker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.quizMaker.IQuizMakerRepository
import ru.iandreyshev.light.domain.quizMaker.Quiz
import ru.iandreyshev.light.domain.quizMaker.draft.QuizMakerDraft
import ru.iandreyshev.light.utill.singleLiveEvent
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class QuizMakerViewModel(
    private val scope: Scope
) : ViewModel() {

    private val mRepository by lazy { scope.get<IQuizMakerRepository>() }

    val quiz = MutableLiveData<QuizViewState>()
    val question = MutableLiveData<CurrentQuestionViewState>()
    val variants = MutableLiveData<List<VariantViewState>>()
    val counter = MutableLiveData(CounterViewState(0, 0))

    val exitEvent = voidSingleLiveEvent()
    val showErrorEvent = singleLiveEvent<String>()

    private var mQuizDraft = QuizMakerDraft()
    private var mIsFirstLoadCompleted = false

    fun onCreate() {
        if (!mIsFirstLoadCompleted) {
            mIsFirstLoadCompleted = true

            viewModelScope.launch {
            }
        }
    }

    fun onQuestionChanged(text: String) {
    }

    fun onQuestionDeleted() {
    }

    fun onAddVariant() {

    }

    fun onNext() {
    }

    fun onPrevious() {
    }

    fun save() {
    }

    private fun buildQuizViewState(quiz: Quiz?): QuizMakerDraft {
        quiz ?: return QuizMakerDraft().apply {
            addQuestion()
        }

        return QuizMakerDraft(quiz)
    }

}
