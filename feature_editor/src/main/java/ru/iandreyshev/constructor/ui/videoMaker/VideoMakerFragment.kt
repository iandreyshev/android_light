package ru.iandreyshev.constructor.ui.videoMaker

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import kotlinx.android.synthetic.main.fragment_video_maker.*
import kotlinx.android.synthetic.main.fragment_video_maker.changeFromGalleryButton
import kotlinx.android.synthetic.main.fragment_video_maker.createButton
import kotlinx.android.synthetic.main.fragment_video_maker.exitButton
import kotlinx.android.synthetic.main.lay_video_maker_video_controller.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.constructor.R
import ru.iandreyshev.constructor.navigation.router
import ru.iandreyshev.core_app.BaseFragment
import ru.iandreyshev.core_ui.setFullScreen
import ru.iandreyshev.core_ui.setOrientationPortrait
import ru.iandreyshev.core_ui.setOrientationUnspecified
import ru.iandreyshev.core_ui.toast
import ru.iandreyshev.core_utils.exhaustive
import ru.iandreyshev.core_utils.uiLazy

class VideoMakerFragment : BaseFragment(R.layout.fragment_video_maker) {

    private val mViewModel by viewModel<VideoMakerViewModel> {
        val navArgs by navArgs<VideoMakerFragmentArgs>()
        parametersOf(getScope(R.id.nav_editor), navArgs.videoMakerArgs)
    }
    private val mPickFromGalleryLauncher by uiLazy {
        registerForActivityResult(
            ActivityResultContracts.GetContent(),
            mViewModel::onPickFromGallery
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initPickerControls()

        mViewModel.state.viewObserveWith(::render)
        mViewModel.event(::handleEvent)

        setFullScreen()
        setOrientationPortrait()
    }

    override fun onPause() {
        super.onPause()
        mViewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setOrientationUnspecified()
        setFullScreen(false)
    }

    private fun initMenu() {
        createButton.setOnClickListener { mViewModel.onCreateDraft() }
        exitButton.setOnClickListener { mViewModel.onExit() }
    }

    private fun initPickerControls() {
        changeFromGalleryButton.setOnClickListener {
            mPickFromGalleryLauncher.launch(PICK_FROM_GALLERY_INPUT)
        }
    }

    private fun handleEvent(event: VideoMakerEvent) {
        when (event) {
            is VideoMakerEvent.ShowError -> toast(event.text)
            VideoMakerEvent.PickVideo ->
                mPickFromGalleryLauncher.launch(PICK_FROM_GALLERY_INPUT)
            VideoMakerEvent.Exit -> router.back()
        }.exhaustive
    }

    private fun render(state: VideoMakerViewState) {
        if (playerView.player != state.player) {
            playerView.player = state.player
        }

        changeFromGalleryButton.isVisible = state.hasVideo
        createButton.isVisible = state.hasVideo

        title.text = state.title

        timeline.max = state.duration
        timeline.progress = state.position
        timeline.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, position: Int, fromUser: Boolean) {
                if (fromUser) mViewModel.onSeekTo(position)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })

        editTitleButton.setOnClickListener {
            MaterialDialog(requireContext()).show {
                var newText = ""
                title(R.string.video_maker_title_title)
                input(
                    prefill = state.title,
                    inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
                    maxLength = MAX_NAME_LENGTH
                ) { _, text ->
                    newText = text.toString()
                }
                positiveButton(text = "Save") {
                    mViewModel.onChangeTitle(newText)
                }
                negativeButton(text = "Cancel")
                lifecycleOwner(this@VideoMakerFragment)
            }
        }
    }

    companion object {
        private const val PICK_FROM_GALLERY_INPUT = "video/*"
        private const val MAX_NAME_LENGTH = 100
    }

}
