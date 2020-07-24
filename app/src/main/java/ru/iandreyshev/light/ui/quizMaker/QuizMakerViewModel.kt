package ru.iandreyshev.light.ui.quizMaker

import androidx.lifecycle.ViewModel
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.editor.IDraftRepository
import timber.log.Timber

class QuizMakerViewModel(
    private val scope: Scope
) : ViewModel() {

    init {
        val repository = scope.get<IDraftRepository>()
        Timber.d("Open editor with repository: $repository")
    }

}
