package ru.iandreyshev.player_core.quiz

internal sealed class Effect {

    class ShowPreview(
        val title: String,
        val questionsCount: Int
    ) : Effect()

    class ShowQuestion(
        val question: Question
    ) : Effect()

    class ShowResult(val result: QuizResult) : Effect()

    object Back : Effect()

    object Finish : Effect()

}
