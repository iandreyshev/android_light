package ru.iandreyshev.light.ui.courseList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.course.Course
import ru.iandreyshev.light.domain.course.ICourseRepository
import ru.iandreyshev.light.ui.player.PlayerArgs
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.singleLiveEvent
import ru.iandreyshev.light.utill.uiLazy
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class CourseListViewModel(scope: Scope) : ViewModel() {

    val courses: LiveData<List<Course>> by uiLazy { mCourses }

    val eventOpenCourseEditor = voidSingleLiveEvent()
    val eventOpenPlayer = singleLiveEvent<PlayerArgs>()

    private val mCourses = MutableLiveData(listOf<Course>())

    private val mRepository by uiLazy { scope.get<ICourseRepository>() }

    fun onCreate() {
        viewModelScope.launch {
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

}
