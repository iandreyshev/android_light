package ru.iandreyshev.light.ui.quizMaker

class CurrentQuestionViewState(
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val question: QuestionViewState,
    val counter: CounterViewState,
    val position: Int
)
