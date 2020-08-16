package ru.iandreyshev.player_core.quiz

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.iandreyshev.player_core.utils.just

internal class QuizPlayerActor(
    private val player: IQuizPlayer
) : Actor<State, Wish, Effect> {

    override fun invoke(state: State, wish: Wish): Observable<out Effect> =
        when (state.type) {
            State.Type.DISABLED -> when (wish) {
                is Wish.Start -> when (wish.quiz.result) {
                    QuizResult.UNDEFINED -> {
                        player.prepare(wish.quiz)
                        Effect.ShowPreview(
                            player.title,
                            player.questionsCount
                        ).just()
                    }
                    else -> Effect.ShowResult(wish.quiz.result).just()
                }
                else -> noEffect()
            }
            State.Type.PREVIEW -> when (wish) {
                Wish.Submit ->
                    Effect.ShowQuestion(player.currentQuestion).just()
                else -> noEffect()
            }
            State.Type.QUESTION -> when (wish) {
                is Wish.SwitchVariantCorrectState -> {
                    player.switchCurrQuestionVariantCorrectState(wish.variantPosition)
                    Effect.ShowQuestion(player.currentQuestion).just()
                }
                Wish.Submit -> when (state.questionResult) {
                    QuestionResult.UNDEFINED -> {
                        player.submitCurrentQuestion()
                        Effect.ShowQuestion(player.currentQuestion).just()
                    }
                    else -> when {
                        state.hasNext() -> {
                            player.moveToNextQuestion()
                            Effect.ShowQuestion(player.currentQuestion).just()
                        }
                        else -> when (val playerResult = player.result) {
                            QuizResult.UNDEFINED -> noEffect()
                            else -> Effect.ShowResult(playerResult).just()
                        }
                    }
                }
                else -> noEffect()
            }
            State.Type.RESULTS -> when (wish) {
                Wish.Submit -> Effect.Finish.just()
                Wish.Back -> Effect.Back.just()
                else -> noEffect()
            }
        }

    private fun noEffect() = Observable.empty<Effect>()

}
