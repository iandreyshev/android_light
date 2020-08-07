package ru.iandreyshev.constructor.ui.editor

import androidx.lifecycle.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.image.ImageDraftId
import ru.iandreyshev.constructor.domain.image.ImageSource
import ru.iandreyshev.constructor.domain.quiz.QuizDraftId
import ru.iandreyshev.constructor.domain.video.VideoDraftId
import ru.iandreyshev.constructor.ui.imageMaker.ImageMakerArgs
import ru.iandreyshev.constructor.ui.quizMaker.QuizMakerArgs
import ru.iandreyshev.constructor.ui.videoMaker.VideoMakerArgs
import ru.iandreyshev.constructor.utils.newUID
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.core_ui.singleLiveEvent
import ru.iandreyshev.core_ui.voidSingleLiveEvent
import ru.iandreyshev.core_utils.uiLazy
import java.lang.IllegalStateException

class EditorViewModel(
    scope: Scope,
    editorArgs: EditorArgs
) : ViewModel() {

    val courseTitle by uiLazy { mCourseName.distinctUntilChanged() }
    val timelineAdapter = GroupAdapter<GroupieViewHolder>()
    val isTimelineEmpty: LiveData<Boolean> by uiLazy { mIsTimelineEmpty }

    val eventOpenQuizMaker = singleLiveEvent<QuizMakerArgs>()
    val eventOpenImageMaker = singleLiveEvent<ImageMakerArgs>()
    val eventOpenVideoMaker = singleLiveEvent<VideoMakerArgs>()
    val eventBackFromEditor = voidSingleLiveEvent()

    private val mRepository by uiLazy {
        scope.get<ICourseDraftRepository> {
            parametersOf(editorArgs)
        }
    }
    private lateinit var mDraft: CourseDraft

    private val mCourseName = MutableLiveData(editorArgs.courseTitle)
    private val mIsTimelineEmpty = MutableLiveData(true)

    fun onCreate() {
        viewModelScope.launch {
            mDraft = mRepository.get()
            mRepository.observeItems()
                .collect { updateTimeline(it) }
        }
    }

    fun onCreateQuiz() {
        eventOpenQuizMaker(
            QuizMakerArgs(
                quizDraftId = QuizDraftId(newUID())
            )
        )
    }

    fun onCreateImage() {
        eventOpenImageMaker(
            ImageMakerArgs(
                courseDraftId = mDraft.id,
                imageDraftId = ImageDraftId(newUID())
            )
        )
    }

    fun onCreateVideo() {
        eventOpenVideoMaker(
            VideoMakerArgs(
                courseDraftId = mDraft.id,
                videoDraftId = VideoDraftId(newUID())
            )
        )
    }

    fun onExit() {
        eventBackFromEditor()
    }

    fun onSave() {
        viewModelScope.launch {
            mRepository.save(mDraft)
            eventBackFromEditor()
        }
    }

    fun onMove(from: Int, to: Int) {
    }

    fun onRenameCourse(newName: String) {
        mDraft = mDraft.copy(title = newName)
        mCourseName.value = mDraft.title
    }

    private fun updateTimeline(items: List<DraftItem>) {
        mIsTimelineEmpty.value = items.isEmpty()
        timelineAdapter.update(items.map { item ->
            when (item) {
                is DraftItem.Quiz -> QuizItem(
                    id = item.draft.hashCode().toLong(),
                    questionsCount = item.draft.questions.count(),
                    onClickListener = {}
                )
                is DraftItem.Image -> {
                    val filePath = when (val source = item.draft.source) {
                        is ImageSource.Gallery -> source.filePath
                        is ImageSource.Photo -> source.filePath
                        null -> throw IllegalStateException("Image source is null")
                    }

                    ImageItem(
                        id = item.draft.id.hashCode().toLong(),
                        imageName = filePath,
                        imageUrl = filePath,
                        onClickListener = {}
                    )
                }
                is DraftItem.Video -> VideoItem(
                    id = item.draft.hashCode().toLong(),
                    videoName = item.draft.source?.filePath.orEmpty(),
                    duration = "",
                    onClickListener = {}
                )
            }
        })
    }

}