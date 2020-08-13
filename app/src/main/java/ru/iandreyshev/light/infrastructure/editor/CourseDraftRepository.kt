package ru.iandreyshev.light.infrastructure.editor

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.iandreyshev.constructor.domain.course.CourseDraftId
import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.infrastructure.ICourseDraftStorage
import ru.iandreyshev.light.domain.IDraftSerializer
import timber.log.Timber

class CourseDraftRepository(
    id: CourseDraftId,
    defaultTitle: String,
    private val storage: ICourseDraftStorage,
    private val serializer: IDraftSerializer
) : ICourseDraftRepository {

    private var mDraft = CourseDraft(id, defaultTitle)
    private val mItemsChannel = ConflatedBroadcastChannel<List<DraftItem>>()

    init {
        mItemsChannel.offer(mDraft.items)
    }

    override suspend fun get(): CourseDraft {
        return mDraft
    }

    override suspend fun getItems(): List<DraftItem> {
        return get().items
    }

    override suspend fun observeItems() =
        mItemsChannel.asFlow()

    override suspend fun save(draft: CourseDraft) {
        val draftToSave = draft
            .copy(items = mDraft.items)
        serializer.save(draftToSave)
    }

    override suspend fun add(item: DraftItem) {
        mDraft = mDraft.copy(
            items = mDraft.items
                .toMutableList()
                .apply { add(item) }
        )
        mItemsChannel.offer(mDraft.items)
    }

    override suspend fun release() {
        Timber.d("Release course draft repository")
        storage.clear()
    }

}
