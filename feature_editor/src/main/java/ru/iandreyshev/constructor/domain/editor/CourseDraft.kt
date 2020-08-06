package ru.iandreyshev.constructor.domain.editor

import ru.iandreyshev.constructor.domain.course.CourseDraftId

data class CourseDraft(
    val id: CourseDraftId,
    val title: String,
    val items: List<DraftItem>
)
