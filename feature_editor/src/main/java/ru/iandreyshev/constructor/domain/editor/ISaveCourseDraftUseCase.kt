package ru.iandreyshev.constructor.domain.editor

interface ISaveCourseDraftUseCase {
    suspend operator fun invoke(draft: CourseDraft)
}
