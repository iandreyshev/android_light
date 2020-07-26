package ru.iandreyshev.light.domain.course

data class Course(
    val id: CourseId,
    val title: String,
    val items: List<CourseItem>
)
