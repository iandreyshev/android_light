package ru.iandreyshev.light.ui.player

import ru.iandreyshev.light.ui.player.quiz.mvi.QuizPlayerFeature

sealed class CourseItemState {

    data class Image(val uri: String) : CourseItemState()

    data class Quiz(
        val feature: QuizPlayerFeature
    ) : CourseItemState() {

        override fun dispose() {
            feature.dispose()
        }

    }

    open fun dispose() = Unit

}
