package ru.iandreyshev.light.ui.courseList

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import dev.chrisbanes.insetter.applySystemWindowInsetsToMargin
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import kotlinx.android.synthetic.main.fragment_course_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.core_app.BaseFragment
import ru.iandreyshev.core_ui.dismissOnDestroy
import ru.iandreyshev.core_ui.doOnApplyWindowInsets
import ru.iandreyshev.core_ui.toast
import ru.iandreyshev.core_utils.exhaustive
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
    private val mCourseListDecorator = CourseListRecyclerViewDecorator()
    private var mCourseContextMenu: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCreateButton()
        initToolbar()
        initCourseList()

        mViewModel.state.viewObserveWith(::render)
        mViewModel.event(::handleEvent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCourseContextMenu?.dismissOnDestroy()
    }

    private fun initCreateButton() {
        createCourseButton.applySystemWindowInsetsToMargin(bottom = true)
        createCourseButton.setOnClickListener {
            mViewModel.onCreateCourseClick()
        }
    }

    private fun initToolbar() {
        appBarLayout.applySystemWindowInsetsToPadding(top = true)
    }

    private fun initCourseList() {
        courseList.adapter = mCourseListAdapter
        courseList.addItemDecoration(mCourseListDecorator)
        courseList.doOnApplyWindowInsets { _, insets, _ ->
            mCourseListDecorator.lastItemBottomMargin = insets.systemWindowInsetBottom
            insets
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

    private fun handleEvent(event: CourseListEvent) {
        when (event) {
            CourseListEvent.OpenCourseEditor -> router.openCourseEditor()
            is CourseListEvent.OpenPlayer -> router.openPlayer(event.args)
            is CourseListEvent.ShowError -> toast(event.text)
        }.exhaustive
    }

    private fun render(state: CourseListViewState) {
        mCourseContextMenu?.dismissOnDestroy()

        courseList.isVisible = state.courses.isNotEmpty()
        emptyViewTitle.isVisible = state.courses.isEmpty()
        emptyViewSubtitle.isVisible = state.courses.isEmpty()
        emptyViewDownloadDemoButton.isVisible = state.courses.isEmpty()
        emptyViewDownloadDemoButton.setOnClickListener {
            mViewModel.onDownloadDemo()
        }
        mCourseListAdapter.submitList(state.courses)
    }

}
