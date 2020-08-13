package ru.iandreyshev.constructor.ui.quizMaker

sealed class QuizMakerEvent {
    data class ShowError(val text: String) : QuizMakerEvent()
    object Exit : QuizMakerEvent()
}