package ru.iandreyshev.constructor.domain.editor

import ru.iandreyshev.constructor.domain.videoMaker.ISaveVideoDraftUseCase
import ru.iandreyshev.constructor.domain.videoMaker.VideoDraft

class AddVideoDraftToCourseUseCase(
    private val courseDraft: CourseDraft
) : ISaveVideoDraftUseCase {

    override suspend fun invoke(draft: VideoDraft) {
        courseDraft.add(draft)
    }

}