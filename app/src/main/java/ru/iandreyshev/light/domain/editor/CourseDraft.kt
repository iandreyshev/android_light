package ru.iandreyshev.light.domain.editor

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.iandreyshev.light.domain.imageMaker.draft.ImageDraft
import ru.iandreyshev.light.domain.quizMaker.draft.QuizDraft
import ru.iandreyshev.light.domain.videoMaker.VideoDraft
import timber.log.Timber

class CourseDraft {

    val items: List<DraftItem>
        get() = mItems

    private val mItems = mutableListOf<DraftItem>()
    private val mChannel = ConflatedBroadcastChannel<List<DraftItem>>()

    fun getItemsObservable() =
        mChannel.asFlow()

    fun add(draft: QuizDraft) {
        mItems.add(DraftItem.Quiz(draft))
        notifyItemsChanged()
    }

    fun add(draft: ImageDraft) {
        mItems.add(DraftItem.Image(draft))
        notifyItemsChanged()
    }

    fun add(draft: VideoDraft) {
        mItems.add(DraftItem.Video(draft))
        notifyItemsChanged()
    }

    fun move(from: Int, to: Int) {
        val item = mItems.getOrNull(from) ?: return
        mItems.removeAt(from)
        mItems.add(to, item)
        Timber.d("Move from $from, to $to")
        notifyItemsChanged()
    }

    private fun notifyItemsChanged() {
        mChannel.offer(mItems)
    }

}
