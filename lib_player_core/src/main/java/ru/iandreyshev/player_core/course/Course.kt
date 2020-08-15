package ru.iandreyshev.player_core.course

import java.util.*

data class Course(
    val id: CourseId,
    val title: String,
    val creationDate: Date,
    val items: List<CourseItem>
)
