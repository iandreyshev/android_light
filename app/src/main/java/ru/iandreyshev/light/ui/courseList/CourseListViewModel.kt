package ru.iandreyshev.light.ui.courseList

import androidx.lifecycle.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.core_ui.singleLiveEvent
import ru.iandreyshev.core_ui.voidSingleLiveEvent
import ru.iandreyshev.core_utils.uiLazy
import ru.iandreyshev.light.ui.player.PlayerArgs
import ru.iandreyshev.core_ui.invoke
import ru.iandreyshev.player_core.course.Course
import ru.iandreyshev.light.domain.ICourseRepository

class CourseListViewModel(scope: Scope) : ViewModel() {

    val courses: LiveData<List<Course>> by uiLazy { mCourses }
    val isListEmpty by uiLazy { courses.map { it.isEmpty() } }

    val eventOpenCourseEditor = voidSingleLiveEvent()
    val eventOpenPlayer = singleLiveEvent<PlayerArgs>()

    private val mCourses = MutableLiveData(listOf<Course>())

    private val mRepository by uiLazy { scope.get<ICourseRepository>() }

    fun onCreate() {
        viewModelScope.launch {
            mRepository.prepare()
            mRepository.getCoursesObservable()
                .collect { courses ->
                    mCourses.value = courses
                }
        }
    }

    fun onCreateCourseClick() {
        eventOpenCourseEditor()
    }

    fun onCourseClickAt(position: Int) {
        val course = mCourses.value
            ?.getOrNull(position) ?: return

        eventOpenPlayer(
            PlayerArgs(courseId = course.id)
        )
    }

    fun onEditCourseAt(position: Int) {
    }

    fun onDeleteCourseAt(position: Int) {
        val course = mCourses.value
            ?.getOrNull(position) ?: return

        mRepository.delete(course.id)
    }

}
