package ru.iandreyshev.light.ui.courseList

import androidx.lifecycle.ViewModel
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.editor.IDraftRepository
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.voidSingleLiveEvent
import timber.log.Timber

class CourseListViewModel(
    private val scope: Scope
) : ViewModel() {

    init {
        val repository = scope.get<IDraftRepository>()
        Timber.d("Open quiz maker with repository: $repository")
    }

    val openCourseEditorEvent = voidSingleLiveEvent()

    fun onCreateCourseClick() {
        openCourseEditorEvent()
    }

}
