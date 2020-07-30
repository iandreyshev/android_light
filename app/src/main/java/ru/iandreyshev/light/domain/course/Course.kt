package ru.iandreyshev.light.domain.course

data class Course(
    val id: CourseId,
    val title: String,
    val creationDate: Any,
    val items: List<CourseItem>
)
