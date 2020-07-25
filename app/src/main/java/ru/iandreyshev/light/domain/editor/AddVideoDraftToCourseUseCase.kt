package ru.iandreyshev.light.domain.editor

import ru.iandreyshev.light.domain.videoMaker.ISaveVideoDraftUseCase
import ru.iandreyshev.light.domain.videoMaker.VideoDraft

class AddVideoDraftToCourseUseCase(
    private val courseDraft: CourseDraft
) : ISaveVideoDraftUseCase {

    override suspend fun invoke(draft: VideoDraft) {
        courseDraft.add(draft)
    }

}