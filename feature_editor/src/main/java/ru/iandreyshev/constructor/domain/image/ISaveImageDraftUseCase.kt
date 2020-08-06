package ru.iandreyshev.constructor.domain.image

import ru.iandreyshev.constructor.domain.image.draft.ImageDraft

interface ISaveImageDraftUseCase {
    suspend operator fun invoke(draft: ImageDraft)
}
