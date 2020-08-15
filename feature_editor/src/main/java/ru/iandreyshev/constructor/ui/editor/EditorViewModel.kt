package ru.iandreyshev.constructor.ui.editor

import androidx.lifecycle.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.iandreyshev.constructor.domain.course.ICourseDraftRepository
import ru.iandreyshev.constructor.domain.editor.CourseDraft
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.domain.image.ImageDraftId
import ru.iandreyshev.constructor.domain.quiz.QuizDraftId
import ru.iandreyshev.constructor.domain.video.VideoDraftId
import ru.iandreyshev.constructor.ui.imageMaker.ImageMakerArgs
import ru.iandreyshev.constructor.ui.quizMaker.QuizMakerArgs
import ru.iandreyshev.constructor.ui.videoMaker.VideoMakerArgs
import ru.iandreyshev.constructor.utils.newUID
import ru.iandreyshev.core_app.UnifiedStateViewModel
import ru.iandreyshev.core_utils.uiLazy

class EditorViewModel(
    scope: Scope,
    editorArgs: EditorArgs
) : UnifiedStateViewModel<EditorViewState, EditorEvent>(
    initialState = EditorViewState(
        courseTitle = editorArgs.courseTitle
    )
) {

    private val mRepository by uiLazy {
        scope.get<ICourseDraftRepository> {
            parametersOf(editorArgs)
        }
    }
    private lateinit var mDraft: CourseDraft

    override fun onCleared() {
        if (mDraft.items.isEmpty()) {
            GlobalScope.launch { mRepository.clear() }
        }
    }

    fun onCreate() {
        viewModelScope.launch {
            mDraft = mRepository.get()
            mRepository.observeItems()
                .collect { updateTimeline(it) }
        }
    }

    fun onCreateQuiz() {
        event {
            EditorEvent.OpenQuizMaker(
                args = QuizMakerArgs(quizDraftId = QuizDraftId(newUID()))
            )
        }
    }

    fun onCreateImage() {
        event {
            EditorEvent.OpenImageMaker(
                ImageMakerArgs(
                    courseDraftId = mDraft.id,
                    imageDraftId = ImageDraftId(newUID())
                )
            )
        }
    }

    fun onCreateVideo() {
        event {
            EditorEvent.OpenVideoMaker(
                VideoMakerArgs(
                    courseDraftId = mDraft.id,
                    videoDraftId = VideoDraftId(newUID())
                )
            )
        }
    }

    fun onExit() {
        viewModelScope.launch {
            mRepository.clear()
            event { EditorEvent.Exit }
        }
    }

    fun onSave() {
         viewModelScope.launch {
            mRepository.save(mDraft)
            event { EditorEvent.Exit }
        }
    }

    fun onRenameCourse(newName: String) {
        mDraft = mDraft.copy(title = newName)
        modifyState {
            copy(courseTitle = mDraft.title)
        }
    }

    private fun updateTimeline(items: List<DraftItem>) {
        modifyState { copy(items = items) }
    }

}