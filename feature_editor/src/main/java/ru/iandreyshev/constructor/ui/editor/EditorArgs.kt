package ru.iandreyshev.constructor.ui.editor

import ru.iandreyshev.constructor.domain.course.CourseDraftId

data class EditorArgs(
    val courseDraftId: CourseDraftId,
    val courseTitle: String
)
