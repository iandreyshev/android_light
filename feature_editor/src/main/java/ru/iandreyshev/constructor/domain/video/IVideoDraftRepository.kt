package ru.iandreyshev.constructor.domain.video

import ru.iandreyshev.constructor.domain.video.draft.VideoDraft

interface IVideoDraftRepository {
    suspend fun get(): VideoDraft
    suspend fun getVideoFilePath(): String
    suspend fun save(draft: VideoDraft)
    suspend fun release()
}
