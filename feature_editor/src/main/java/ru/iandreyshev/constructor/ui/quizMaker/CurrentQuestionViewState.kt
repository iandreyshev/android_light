package ru.iandreyshev.constructor.ui.quizMaker

data class CurrentQuestionViewState(
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val question: QuestionViewState,
    val counter: CounterViewState,
    val position: Int
)
