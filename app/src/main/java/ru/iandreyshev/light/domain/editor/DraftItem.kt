package ru.iandreyshev.light.domain.editor

import ru.iandreyshev.light.domain.imageMaker.draft.ImageDraft
import ru.iandreyshev.light.domain.quizMaker.draft.QuizDraft
import ru.iandreyshev.light.domain.videoMaker.VideoDraft

sealed class DraftItem {
    class Quiz(val draft: QuizDraft) : DraftItem()
    class Image(val draft: ImageDraft) : DraftItem()
    class Video(val draft: VideoDraft) : DraftItem()
}
