package ru.iandreyshev.light.ui.editor

import androidx.lifecycle.ViewModel
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.editor.IDraftRepository
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.voidSingleLiveEvent
import timber.log.Timber

class EditorViewModel(scope: Scope) : ViewModel() {

    val eventOpenQuizMaker = voidSingleLiveEvent()
    val eventOpenImageMaker = voidSingleLiveEvent()
    val eventOpenVideoMaker = voidSingleLiveEvent()
    val eventBackFromEditor = voidSingleLiveEvent()

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

}