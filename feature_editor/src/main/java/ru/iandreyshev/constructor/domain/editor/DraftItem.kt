package ru.iandreyshev.constructor.domain.editor

import ru.iandreyshev.constructor.domain.image.draft.ImageDraft
import ru.iandreyshev.constructor.domain.quiz.draft.QuizDraft
import ru.iandreyshev.constructor.domain.video.draft.VideoDraft

sealed class DraftItem {
    class Quiz(val draft: QuizDraft) : DraftItem()
    class Image(val draft: ImageDraft) : DraftItem()
    class Video(val draft: VideoDraft) : DraftItem()

    inline fun <reified T : DraftItem> asTypeOrNull() =
        when (this is T) {
            true -> this
            else -> null
        }

}
