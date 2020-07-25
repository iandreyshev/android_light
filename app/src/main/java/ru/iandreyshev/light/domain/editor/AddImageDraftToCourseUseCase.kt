package ru.iandreyshev.light.domain.editor

import ru.iandreyshev.light.domain.imageMaker.ISaveImageDraftUseCase
import ru.iandreyshev.light.domain.imageMaker.draft.ImageDraft

class AddImageDraftToCourseUseCase(
    private val courseDraft: CourseDraft
) : ISaveImageDraftUseCase {

    override suspend fun invoke(draft: ImageDraft) {
        courseDraft.add(draft)
    }

}
