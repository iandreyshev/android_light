package ru.iandreyshev.light.domain.player

import ru.iandreyshev.light.domain.player.quiz.IQuizPlayer

sealed class PlayerCourseItem {

    data class Image(val uri: String) : PlayerCourseItem()

    data class Quiz(val player: IQuizPlayer) : PlayerCourseItem()

    data class Video(val data: Any) : PlayerCourseItem()

}
