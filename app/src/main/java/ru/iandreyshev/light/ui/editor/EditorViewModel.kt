package ru.iandreyshev.light.ui.editor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.editor.CourseDraft
import ru.iandreyshev.light.domain.editor.DraftItem
import ru.iandreyshev.light.domain.editor.ISaveCourseDraftUseCase
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.uiLazy
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class EditorViewModel(
    scope: Scope,
    private val args: EditorArgs
) : ViewModel() {

    val timelineItems by uiLazy { mTimelineItems }

    val eventOpenQuizMaker = voidSingleLiveEvent()
    val eventOpenImageMaker = voidSingleLiveEvent()
    val eventOpenVideoMaker = voidSingleLiveEvent()
    val eventBackFromEditor = voidSingleLiveEvent()

    private val mTimelineItems = MutableLiveData(listOf<TimelineItem>())

    private val mDraft by uiLazy {
        scope.get<CourseDraft> { parametersOf(args.courseTitle) }
    }
    private val mSaveDraft by uiLazy { scope.get<ISaveCourseDraftUseCase>() }

    fun onCreate() {
        viewModelScope.launch {
            mDraft.getItemsObservable().collect {
                mTimelineItems.value = mDraft.items.asTimelineItems()
            }
        }
    }

    fun onOpenQuizMaker() {
        eventOpenQuizMaker()
    }

    fun onOpenImageMaker() {
        eventOpenImageMaker()
    }

    fun onOpenVideoMaker() {
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

    private fun List<DraftItem>.asTimelineItems() =
        map {
            when (it) {
                is DraftItem.Quiz -> TimelineItem.Quiz()
                is DraftItem.Image -> TimelineItem.Image()
                is DraftItem.Video -> TimelineItem.Video()
            }
        }

}