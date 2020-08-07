package ru.iandreyshev.constructor.domain.image

sealed class ImageDraftResult {
    object Success : ImageDraftResult()
    class Error(val error: ImageDraftError) : ImageDraftResult()
}
