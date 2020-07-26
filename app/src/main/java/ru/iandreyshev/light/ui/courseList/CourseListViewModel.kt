package ru.iandreyshev.light.ui.courseList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import ru.iandreyshev.light.domain.courseList.Course
import ru.iandreyshev.light.domain.courseList.ICourseRepository
import ru.iandreyshev.light.utill.invoke
import ru.iandreyshev.light.utill.uiLazy
import ru.iandreyshev.light.utill.voidSingleLiveEvent

class CourseListViewModel(scope: Scope) : ViewModel() {

    val courses: LiveData<List<Course>> by uiLazy { mCourses }

    val openCourseEditorEvent = voidSingleLiveEvent()

    private val mCourses = MutableLiveData(listOf<Course>())

    private var mIsOnCreateComplete = false
    private val mRepository by uiLazy { scope.get<ICourseRepository>() }

    fun onCreate() {
        if (mIsOnCreateComplete) {
            return
        }

        mIsOnCreateComplete = true

        viewModelScope.launch {
            mRepository.getCoursesObservable()
                .collect { courses ->
                    mCourses.value = courses
                }
        }

    }

    fun onCreateCourseClick() {
        openCourseEditorEvent()
    }

}
