package ru.iandreyshev.light.ui.editor

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import kotlinx.android.synthetic.main.fragment_editor.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.system.FeatureToggle
import ru.iandreyshev.light.utill.withItemListeners

class EditorFragment : BaseFragment(R.layout.fragment_editor) {

    private val mViewModel: EditorViewModel by viewModel {
        parametersOf(
            getScope(R.id.nav_editor),
            EditorArgs(
                courseId = null,
                courseTitle = resources.getString(R.string.editor_default_title)
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initTimeline()
        initCourseTitle()
        initSettingsMenu()
    }

    private fun initMenu() {
        toolbar.setNavigationOnClickListener { mViewModel.onExit() }
        toolbar.withItemListeners {
            R.id.actionEditorSave { mViewModel.onSave() }
        }

        addVideoButton.isVisible = FeatureToggle.isVideoEnabled
        addVideoButton.setOnClickListener { mViewModel.onCreateVideo() }
        addImageButton.setOnClickListener { mViewModel.onCreateImage() }
        addQuizButton.setOnClickListener { mViewModel.onCreateQuiz() }

        mViewModel.isTimelineEmpty.viewObserveWith { bottomMenu.isGone = it }

        mViewModel.eventBackFromEditor { router().back() }
        mViewModel.eventOpenImageMaker { router().openImageMaker() }
        mViewModel.eventOpenVideoMaker { router().openVideoMaker() }
        mViewModel.eventOpenQuizMaker { router().openQuizMaker() }
    }

    private fun initTimeline() {
        timeline.adapter = mViewModel.timelineAdapter
        mViewModel.isTimelineEmpty.viewObserveWith {
            emptyView.isVisible = it
            timeline.isVisible = !it
        }
        emptyViewAddImageButton.setOnClickListener { mViewModel.onCreateImage() }
        emptyViewAddQuizButton.setOnClickListener { mViewModel.onCreateQuiz() }
//        buildItemTouchHelper()
//            .attachToRecyclerView(timeline)
    }

    private fun initCourseTitle() {
        mViewModel.courseTitle.viewObserveWith {
            courseTitle.text = it
            courseTitle.setOnClickListener {
                showRenameCourseDialog()
            }
        }
    }

    private fun initSettingsMenu() {
        settingsButton.setOnClickListener { buttonView ->
            val popupMenu = PopupMenu(requireContext(), buttonView)
            popupMenu.inflate(R.menu.menu_editor_settings)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.actionRename -> {
                        showRenameCourseDialog()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
            popupMenus += popupMenu
        }
    }

    private fun buildItemTouchHelper() =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(UP or DOWN, 0) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                mViewModel.onMove(from, to)
                recyclerView.adapter?.notifyItemMoved(from, to)

                return true
            }
        })

    private fun showRenameCourseDialog() {
        MaterialDialog(requireContext()).show {
            title(R.string.editor_rename_dialog_title)
            input(
                prefill = mViewModel.courseTitle.value,
                inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
                maxLength = COURSE_MAX_NAME
            ) { _, text ->
                mViewModel.onRenameCourse(text.toString())
            }
            lifecycleOwner(this@EditorFragment)
        }
    }

    companion object {
        private const val COURSE_MAX_NAME = 72
    }

}
