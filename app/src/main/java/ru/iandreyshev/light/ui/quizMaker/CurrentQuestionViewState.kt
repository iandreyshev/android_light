package ru.iandreyshev.light.ui.quizMaker

class CurrentQuestionViewState(
    val hasNext: Boolean,
    val hasPrevious: Boolean,
    val question: QuestionViewState,
    val order: Int,
    val canDelete: Boolean
)
