package ru.iandreyshev.light.ui.player

import ru.iandreyshev.light.domain.course.CourseId
import java.io.Serializable

class PlayerArgs(
    val courseId: CourseId
) : Serializable
