package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.domain.course.CourseItem
import ru.iandreyshev.light.domain.player.quiz.QuizResult

sealed class PlayerItem {

    data class Image(
        val uri: String,
        val isComplete: Boolean
    ) : PlayerItem()

    data class Quiz(
        val quiz: CourseItem.Quiz,
        val result: QuizResult?
    ) : PlayerItem()

    data class Video(
        val uri: String,
        val isComplete: Boolean
    ) : PlayerItem()

}
