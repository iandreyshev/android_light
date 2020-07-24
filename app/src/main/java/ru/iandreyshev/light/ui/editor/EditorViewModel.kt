package ru.iandreyshev.light.ui.editor

import androidx.lifecycle.ViewModel
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.editor.IDraftRepository
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.voidSingleLiveEvent
import timber.log.Timber

class EditorViewModel(
    private val scope: Scope
) : ViewModel() {

    val openQuizMakerEvent = voidSingleLiveEvent()
    val backFromEditorEvent = voidSingleLiveEvent()

    init {
        val repository = scope.get<IDraftRepository>()
        Timber.d("Open quiz maker with repository: $repository")
    }

    fun onOpenQuizMaker() {
        openQuizMakerEvent()
    }

    fun onExit() {
        backFromEditorEvent()
    }

}