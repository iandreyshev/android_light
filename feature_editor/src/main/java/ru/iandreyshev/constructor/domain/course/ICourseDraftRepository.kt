package ru.iandreyshev.constructor.domain.course

import kotlinx.coroutines.flow.Flow
import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.image.draft.ImageDraft

interface ICourseDraftRepository {
    suspend fun get(): CourseDraft
    suspend fun getItems(): List<DraftItem>
    suspend fun observeItems(): Flow<List<DraftItem>>
    suspend fun save(draft: CourseDraft)
    suspend fun add(item: DraftItem)
    suspend fun release()
}
