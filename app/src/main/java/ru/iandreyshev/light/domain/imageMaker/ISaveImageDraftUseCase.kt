package ru.iandreyshev.light.domain.imageMaker

import ru.iandreyshev.light.domain.imageMaker.draft.ImageDraft

interface ISaveImageDraftUseCase {
    suspend operator fun invoke(draft: ImageDraft)
}
