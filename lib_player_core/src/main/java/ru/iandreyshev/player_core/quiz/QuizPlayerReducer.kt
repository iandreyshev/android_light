package ru.iandreyshev.player_core.quiz

import com.badoo.mvicore.element.Reducer

internal class QuizPlayerReducer : Reducer<State, Effect> {

    override fun invoke(state: State, effect: Effect): State =
        when (state.type) {
            State.Type.DISABLED -> when (effect) {
                is Effect.ShowPreview ->
                    state.copy(
                        type = State.Type.PREVIEW,
                        questionsCount = effect.questionsCount
                    )
                is Effect.ShowResult ->
                    state.copy(
                        type = State.Type.RESULTS,
                        quizResult = effect.result
                    )
                else -> state
            }
            State.Type.PREVIEW -> when (effect) {
                is Effect.ShowQuestion ->
                    state.copy(
                        type = State.Type.QUESTION,
                        questionText = effect.question.text,
                        questionIndex = effect.question.position,
                        isMultipleMode = effect.question.isMultipleMode,
                        variants = effect.question.variants,
                        questionResult = effect.question.result
                    )
                else -> state
            }
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
            State.Type.RESULTS -> when (effect) {
                Effect.Finish ->
                    state.copy(
                        type = State.Type.DISABLED
                    )
                else -> state
            }
        }

}
