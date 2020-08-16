package ru.iandreyshev.light.ui.courseList

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_course_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.core_app.BaseFragment
import ru.iandreyshev.core_ui.dismissOnDestroy
import ru.iandreyshev.core_utils.uiLazy
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router

class CourseListFragment : BaseFragment(R.layout.fragment_course_list) {

    private val mViewModel by viewModel<CourseListViewModel> {
        parametersOf(getScope(R.id.nav_main))
    }
    private val mCourseListAdapter by uiLazy {
        CourseListAdapter(
            onClickListener = mViewModel::onCourseClickAt,
            onLongClickListener = ::showCourseMenu
        )
    }
    private var mCourseContextMenu: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initCourseList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCourseContextMenu?.dismissOnDestroy()
    }

    private fun initMenu() {
        createCourseButton.setOnClickListener {
            mViewModel.onCreateCourseClick()
        }

        mViewModel.isListEmpty.viewObserveWith { createCourseButton.isGone = it }

        mViewModel.eventOpenCourseEditor(router::openCourseEditor)
        mViewModel.eventOpenPlayer(router::openPlayer)
    }

    private fun initCourseList() {
        courseList.adapter = mCourseListAdapter
        mViewModel.courses.viewObserveWith {
            mCourseContextMenu?.dismissOnDestroy()

            courseList.isVisible = it.isNotEmpty()
            emptyViewTitle.isVisible = it.isEmpty()
            emptyViewCreateCourseButton.isVisible = it.isEmpty()
            emptyViewCreateCourseButton.setOnClickListener {
                mViewModel.onCreateCourseClick()
            }
            mCourseListAdapter.submitList(it)
        }
    }

    private fun showCourseMenu(coursePosition: Int) {
        mCourseContextMenu.dismissOnDestroy()
        mCourseContextMenu = AlertDialog.Builder(requireContext())
            .setItems(
                arrayOf(
                    getString(R.string.course_list_course_context_menu_edit),
                    getString(R.string.course_list_course_context_menu_delete)
                )
            ) { dialog, position ->
                when (position) {
                    0 -> mViewModel.onEditCourseAt(coursePosition)
                    1 -> mViewModel.onDeleteCourseAt(coursePosition)
                    else -> Unit
                }
                dialog.dismiss()
            }
            .show()
    }

}
