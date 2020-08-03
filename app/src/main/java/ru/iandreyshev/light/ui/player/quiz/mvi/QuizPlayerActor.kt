package ru.iandreyshev.light.ui.player.quiz.mvi

import com.badoo.mvicore.element.Actor
import io.reactivex.Observable
import ru.iandreyshev.light.domain.player.quiz.IQuizPlayer
import ru.iandreyshev.light.utill.just

class QuizPlayerActor(
    private val player: IQuizPlayer
) : Actor<State, Wish, Effect> {

    override fun invoke(state: State, wish: Wish): Observable<out Effect> =
        when (state.type) {
            State.Type.DISABLED -> when (wish) {
                is Wish.Start -> when (wish.quiz.result) {
                    null -> {
                        player.prepare(wish.quiz)
                        Effect.ShowPreview(player.questionsCount).just()
                    }
                    else ->
                        Effect.ShowResult(wish.quiz.result).just()
                }
                else -> noEffect()
            }
            State.Type.PREVIEW -> when (wish) {
                Wish.Submit ->
                    Effect.ShowQuestion(player.currentQuestion).just()
                else -> noEffect()
            }
            State.Type.QUESTION -> when (wish) {
                is Wish.SwitchVariantValidState -> {
                    player.switchCurrQuestionVariantValidState(wish.variantPosition)
                    Effect.ShowQuestion(player.currentQuestion).just()
                }
                Wish.Submit -> when (state.questionResult) {
                    null -> {
                        player.submitCurrentQuestion()
                        Effect.ShowQuestion(player.currentQuestion).just()
                    }
                    else -> when {
                        state.hasNext() -> {
                            player.moveToNextQuestion()
                            Effect.ShowQuestion(player.currentQuestion).just()
                        }
                        else -> when (val playerResult = player.result) {
                            null -> noEffect()
                            else -> Effect.ShowResult(playerResult).just()
                        }
                    }
                }
                else -> noEffect()
            }
            State.Type.RESULTS -> when (wish) {
                Wish.Submit -> {
                    player.onFinish()
                    Effect.Finish.just()
                }
                else -> noEffect()
            }
        }

    private fun noEffect() = Observable.empty<Effect>()

}
