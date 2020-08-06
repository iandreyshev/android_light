package ru.iandreyshev.constructor.ui.quizMaker

import ru.iandreyshev.constructor.domain.quiz.QuizDraftId
import java.io.Serializable

data class QuizMakerArgs(
    val quizDraftId: QuizDraftId
): Serializable
