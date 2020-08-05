package ru.iandreyshev.player_core.course

data class Course(
    val id: CourseId,
    val title: String,
    val creationDate: Any,
    val items: List<CourseItem>
)
