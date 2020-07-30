package ru.iandreyshev.light.ui.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.editor.CourseDraft
import ru.iandreyshev.light.domain.editor.DraftItem
import ru.iandreyshev.light.domain.editor.ISaveCourseDraftUseCase
import ru.iandreyshev.light.utill.distinctUntilChanged
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.uiLazy
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class EditorViewModel(
    scope: Scope,
    private val args: EditorArgs
) : ViewModel() {

    val courseTitle by uiLazy { mCourseName.distinctUntilChanged() }
    val timelineAdapter = GroupAdapter<GroupieViewHolder>()
    val isTimelineEmpty: LiveData<Boolean> by uiLazy { mIsTimelineEmpty }

    val eventOpenQuizMaker = voidSingleLiveEvent()
    val eventOpenImageMaker = voidSingleLiveEvent()
    val eventOpenVideoMaker = voidSingleLiveEvent()
    val eventBackFromEditor = voidSingleLiveEvent()

    private val mCourseName = MutableLiveData(args.courseTitle)
    private val mIsTimelineEmpty = MutableLiveData(true)

    private val mDraft by uiLazy {
        scope.get<CourseDraft> { parametersOf(args.courseTitle) }
    }
    private val mSaveDraft by uiLazy { scope.get<ISaveCourseDraftUseCase>() }

    fun onCreate() {
        viewModelScope.launch {
            mDraft.getItemsObservable()
                .collect { updateTimeline(it) }
        }
    }

    fun onCreateQuiz() {
        eventOpenQuizMaker()
    }

    fun onCreateImage() {
        eventOpenImageMaker()
    }

    fun onCreateVideo() {
        eventOpenVideoMaker()
    }

    fun onExit() {
        eventBackFromEditor()
    }

    fun onSave() {
        viewModelScope.launch {
            mSaveDraft(mDraft)
            eventBackFromEditor()
        }
    }

    fun onMove(from: Int, to: Int) {
        mDraft.move(from, to)
    }

    fun onRenameCourse(newName: String) {
        mDraft.title = newName
        mCourseName.value = mDraft.title
    }

    private fun updateTimeline(items: List<DraftItem>) {
        mIsTimelineEmpty.value = items.isEmpty()
        timelineAdapter.update(items.map { item ->
            when (item) {
                is DraftItem.Quiz -> QuizItem(
                    id = item.draft.hashCode().toLong(),
                    questionsCount = item.draft.questionsCount,
                    onClickListener = {}
                )
                is DraftItem.Image -> ImageItem(
                    id = item.draft.hashCode().toLong(),
                    imageName = item.draft.fileName,
                    imageUrl = "",
                    onClickListener = {}
                )
                is DraftItem.Video -> VideoItem(
                    id = item.draft.hashCode().toLong(),
                    videoName = "",
                    duration = "",
                    onClickListener = {}
                )
            }
        })
    }

}