package ru.iandreyshev.constructor.domain.image.draft

import ru.iandreyshev.constructor.domain.image.ImageDraftId
import ru.iandreyshev.constructor.domain.image.ImageSource

data class ImageDraft(
    val id: ImageDraftId,
    val text: String? = null,
    val source: ImageSource? = null
)
