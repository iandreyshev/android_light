package ru.iandreyshev.light.ui.player.quiz.mvi

import ru.iandreyshev.light.domain.player.quiz.Question
import ru.iandreyshev.light.domain.player.quiz.QuizResult

sealed class Effect {

    class ShowPreview(val questionsCount: Int) : Effect()

    class ShowQuestion(
        val question: Question
    ) : Effect()

    class ShowResult(val result: QuizResult) : Effect()

    object Finish : Effect()

}
