package ru.iandreyshev.light.domain.videoMaker

interface ISaveVideoDraftUseCase {
    suspend operator fun invoke(draft: VideoDraft)
}
