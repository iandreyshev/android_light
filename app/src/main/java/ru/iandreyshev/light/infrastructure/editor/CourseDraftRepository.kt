package ru.iandreyshev.light.infrastructure.editor

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.iandreyshev.constructor.domain.course.CourseDraftId
import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.image.ImageSource
import ru.iandreyshev.constructor.domain.video.VideoSource
import ru.iandreyshev.constructor.utils.newUID
import ru.iandreyshev.light.domain.DraftDataSource
import ru.iandreyshev.light.domain.ICourseRepository
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.player_core.course.CourseId
import ru.iandreyshev.player_core.course.CourseItem
import ru.iandreyshev.player_core.quiz.Question
import ru.iandreyshev.player_core.quiz.QuestionId
import ru.iandreyshev.player_core.quiz.Variant
import ru.iandreyshev.player_core.quiz.VariantId
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.util.*

class CourseDraftRepository(
    private val courseDraftId: CourseDraftId?,
    private val courseDefaultTitle: String,
    private val repository: ICourseRepository
) : ICourseDraftRepository {

    private var mDraft = CourseDraft(
        CourseDraftId(newUID()),
        courseDefaultTitle,
        listOf()
    )
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
        val course = draft
            .copy(items = mDraft.items)
            .asCourse()
        repository.save(course)
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
    }

    private fun CourseDraft.asCourse() = Course(
        id = CourseId(id.value),
        title = title,
        creationDate = Date(),
        items = items.map { item ->
            when (item) {
                is DraftItem.Quiz ->
                    CourseItem.Quiz(
                        item.draft.questions
                            .mapIndexed { pos, questionDraft ->
                                Question(
                                    id = QuestionId(questionDraft.id.value),
                                    text = questionDraft.text,
                                    variants = questionDraft.variants.map { variantDraft ->
                                        Variant(
                                            id = VariantId(variantDraft.id.value),
                                            text = variantDraft.text,
                                            isValid = variantDraft.isValid,
                                            isSelectedAsValid = true
                                        )
                                    },
                                    isMultipleMode = questionDraft.isMultipleMode,
                                    position = pos,
                                    result = null
                                )
                            })
                is DraftItem.Image ->
                    CourseItem.Image(
                        item.draft.text,
                        item.draft.source
                            ?.asPlayerImageSource()
                            ?: throw IllegalArgumentException("Image draft source is null")
                    )
                is DraftItem.Video ->
                    CourseItem.Video(
                        item.draft.source
                            ?.asPlayerVideoSource()
                            ?: throw IllegalArgumentException("Video draft source is null")
                    )
            }
        }
    )


    private fun ImageSource.asPlayerImageSource() =
        ru.iandreyshev.player_core.image.ImageSource(filePath)

    private fun VideoSource.asPlayerVideoSource() =
        ru.iandreyshev.player_core.video.VideoSource(filePath)

}
