package ru.iandreyshev.constructor.ui.editor

import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.core_utils.uiLazy

data class EditorViewState(
    val courseTitle: String,
    val items: List<DraftItem> = listOf()
) {

    val isTimelineEmpty by uiLazy { items.isEmpty() }

    companion object {
        fun from(args: EditorArgs) =
            EditorViewState(courseTitle = args.courseTitle)
    }

}
