package ru.iandreyshev.constructor.domain.videoMaker

interface ISaveVideoDraftUseCase {
    suspend operator fun invoke(draft: VideoDraft)
}
