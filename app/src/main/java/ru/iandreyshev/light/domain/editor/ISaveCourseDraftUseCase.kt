package ru.iandreyshev.light.domain.editor

interface ISaveCourseDraftUseCase {
    suspend operator fun invoke(draft: CourseDraft)
}
