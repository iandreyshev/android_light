package ru.iandreyshev.light.ui.player.quiz.mvi

import com.badoo.mvicore.element.Reducer

class QuizPlayerReducer : Reducer<State, Effect> {

    override fun invoke(state: State, effect: Effect): State =
        when (state.type) {
            State.Type.QUESTION -> when (effect) {
                is Effect.ShowQuestion ->
                    state.copy(
                        type = State.Type.QUESTION,
                        questionText = effect.question.text,
                        questionIndex = effect.question.position,
                        isMultipleMode = effect.question.isMultipleMode,
                        variants = effect.question.variants,
                        questionResult = effect.question.result
                    )
                is Effect.ShowResult ->
                    state.copy(
                        type = State.Type.RESULTS,
                        quizResult = effect.result
                    )
                else -> state
            }
            State.Type.RESULTS -> state
        }

}
