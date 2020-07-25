package ru.iandreyshev.light.domain.editor

import ru.iandreyshev.light.domain.imageMaker.draft.ImageDraft
import ru.iandreyshev.light.domain.quizMaker.draft.QuizDraft
import ru.iandreyshev.light.domain.videoMaker.VideoDraft

class CourseDraft {
    fun add(draft: QuizDraft) {}
    fun add(draft: ImageDraft) {}
    fun add(draft: VideoDraft) {}
}
