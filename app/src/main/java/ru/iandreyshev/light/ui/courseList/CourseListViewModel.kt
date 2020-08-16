package ru.iandreyshev.light.ui.courseList

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.core_app.UnifiedStateViewModel
import ru.iandreyshev.core_utils.uiLazy
import ru.iandreyshev.light.ui.player.PlayerArgs
import ru.iandreyshev.light.domain.ICourseRepository

class CourseListViewModel(
    scope: Scope
) : UnifiedStateViewModel<CourseListViewState, CourseListEvent>(
    initialState = CourseListViewState(
        courses = emptyList()
    )
) {

    private val mRepository by uiLazy { scope.get<ICourseRepository>() }

    fun onCreate() {
        viewModelScope.launch {
            mRepository.prepare()
            mRepository.getCoursesObservable()
                .collect { courses ->
                    modifyState {
                        copy(courses = courses)
                    }
                }
        }
    }

    fun onCreateCourseClick() {
        event { CourseListEvent.OpenCourseEditor }
    }

    fun onCourseClickAt(position: Int) {
        val course = state.value
            ?.courses
            ?.getOrNull(position)
            ?: return

        event {
            CourseListEvent.OpenPlayer(
                args = PlayerArgs(courseId = course.id)
            )
        }
    }

    fun onDownloadDemo() {
        event { CourseListEvent.ShowError("Sorry, this function is not available yet") }
    }

    fun onEditCourseAt(position: Int) {
        event { CourseListEvent.ShowError("Sorry, this function is not available yet") }
    }

    fun onDeleteCourseAt(position: Int) {
        val course = state.value
            ?.courses
            ?.getOrNull(position)
            ?: return

        mRepository.delete(course.id)
    }

}
