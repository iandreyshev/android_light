package ru.iandreyshev.constructor.domain.editor

import ru.iandreyshev.constructor.domain.image.ISaveImageDraftUseCase
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft

class AddImageDraftToCourseUseCase(
    private val courseDraft: CourseDraft
) : ISaveImageDraftUseCase {

    override suspend fun invoke(draft: ImageDraft) {
        courseDraft.add(draft)
    }

}
