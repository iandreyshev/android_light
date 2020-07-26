package ru.iandreyshev.light.domain.courseList

data class Course(
    val id: String,
    val title: String,
    val items: List<CourseItem>
)
