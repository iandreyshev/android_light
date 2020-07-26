package ru.iandreyshev.light.ui.courseList

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_course_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.utill.uiLazy

class CourseListFragment : BaseFragment(R.layout.fragment_course_list) {

    private val mViewModel by viewModel<CourseListViewModel> {
        parametersOf(getScope(R.id.nav_main))
    }
    private val mCourseListAdapter by uiLazy {
        CourseListAdapter(mViewModel::onCourseClickAt)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initCourseList()
    }

    private fun initMenu() {
        createCourseButton.setOnClickListener {
            mViewModel.onCreateCourseClick()
        }

        mViewModel.eventOpenCourseEditor(router()::openCourseEditor)
        mViewModel.eventOpenPlayer(router()::openPlayer)
    }

    private fun initCourseList() {
        courseList.adapter = mCourseListAdapter
        mViewModel.courses.viewObserveWith(mCourseListAdapter::submitList)
    }

}