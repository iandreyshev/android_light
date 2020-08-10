package ru.iandreyshev.player_core.course

import ru.iandreyshev.player_core.quiz.QuizResult

sealed class PlayerItem {

    data class Image(
        val uri: String,
        val isComplete: Boolean
    ) : PlayerItem()

    data class Quiz(
        val quiz: CourseItem.Quiz,
        val result: QuizResult
    ) : PlayerItem()

    data class Video(
        val uri: String,
        val isComplete: Boolean
    ) : PlayerItem()

}
