package ru.iandreyshev.light.ui.courseList

import androidx.lifecycle.ViewModel
import org.koin.core.scope.Scope
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class CourseListViewModel(
    private val scope: Scope
) : ViewModel() {

    val openCourseEditorEvent = voidSingleLiveEvent()

    fun onCreateCourseClick() {
        openCourseEditorEvent()
    }

}
