package ru.iandreyshev.constructor.ui.editor

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_editor.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.constructor.R
import ru.iandreyshev.constructor.domain.course.CourseDraftId
import ru.iandreyshev.constructor.domain.editor.DraftItem
import ru.iandreyshev.constructor.navigation.router
import ru.iandreyshev.core_app.BaseFragment
import ru.iandreyshev.core_ui.withItemListeners
import ru.iandreyshev.core_utils.exhaustive
import java.lang.IllegalStateException

class EditorFragment : BaseFragment(R.layout.fragment_editor) {

    private val mViewModel: EditorViewModel by viewModel {
        parametersOf(
            getScope(R.id.nav_editor),
            EditorArgs(
                courseDraftId = CourseDraftId.random(),
                courseTitle = resources.getString(R.string.editor_default_title)
            )
        )
    }
    private val mTimelineAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initTimeline()

        mViewModel.state.viewObserveWith(::render)
        mViewModel.event(::handleEvent)
    }

    private fun initMenu() {
        toolbar.setNavigationOnClickListener { mViewModel.onExit() }
        toolbar.withItemListeners {
            R.id.actionEditorSave { mViewModel.onSave() }
        }

        addVideoButton.setOnClickListener { mViewModel.onCreateVideo() }
        addImageButton.setOnClickListener { mViewModel.onCreateImage() }
        addQuizButton.setOnClickListener { mViewModel.onCreateQuiz() }
    }

    private fun initTimeline() {
        timeline.adapter = mTimelineAdapter
        emptyViewAddVideoButton.setOnClickListener { mViewModel.onCreateVideo() }
        emptyViewAddImageButton.setOnClickListener { mViewModel.onCreateImage() }
        emptyViewAddQuizButton.setOnClickListener { mViewModel.onCreateQuiz() }
    }

    private fun handleEvent(event: EditorEvent) {
        when (event) {
            is EditorEvent.OpenQuizMaker -> router.openQuizMaker(event.args)
            is EditorEvent.OpenImageMaker -> router.openImageMaker(event.args)
            is EditorEvent.OpenVideoMaker -> router.openVideoMaker(event.args)
            EditorEvent.Exit -> router.back()
        }.exhaustive
    }

    private fun render(state: EditorViewState) {
        emptyView.isVisible = state.isTimelineEmpty
        timeline.isVisible = !state.isTimelineEmpty

        bottomMenu.isGone = state.isTimelineEmpty

        courseTitle.text = state.courseTitle
        courseTitle.setOnClickListener(null)
        courseTitle.setOnClickListener {
            showRenameCourseDialog(state.courseTitle)
        }

        settingsButton.setOnClickListener(null)
        settingsButton.setOnClickListener { buttonView ->
            showSettingsMenu(buttonView, state.courseTitle)
        }

        mTimelineAdapter.update(state.items.map { item ->
            when (item) {
                is DraftItem.Quiz -> QuizItem(
                    id = item.draft.hashCode().toLong(),
                    questionsCount = item.draft.questions.count(),
                    onClickListener = {}
                )
                is DraftItem.Image -> {
                    val filePath = item.draft.source?.filePath
                        ?: throw IllegalStateException("Image source is null")

                    ImageItem(
                        id = item.draft.id.hashCode().toLong(),
                        imageName = filePath,
                        imageUrl = filePath,
                        onClickListener = {}
                    )
                }
                is DraftItem.Video -> VideoItem(
                    id = item.draft.hashCode().toLong(),
                    videoName = item.draft.source?.filePath.orEmpty(),
                    duration = "",
                    onClickListener = {}
                )
            }
        })
    }

    private fun showRenameCourseDialog(preFill: String) {
        MaterialDialog(requireContext()).show {
            title(R.string.editor_rename_dialog_title)
            input(
                prefill = preFill,
                inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
                maxLength = COURSE_MAX_NAME
            ) { _, text ->
                mViewModel.onRenameCourse(text.toString())
            }
            negativeButton { }
            lifecycleOwner(this@EditorFragment)
        }
    }

    private fun showSettingsMenu(view: View, courseTitle: String) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.menu_editor_settings)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.actionRename -> {
                    showRenameCourseDialog(courseTitle)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
        popupMenus += popupMenu
    }

    companion object {
        private const val COURSE_MAX_NAME = 72
    }

}
