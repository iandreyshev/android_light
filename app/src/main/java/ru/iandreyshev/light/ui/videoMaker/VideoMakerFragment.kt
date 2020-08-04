package ru.iandreyshev.light.ui.videoMaker

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_video_maker.*
import kotlinx.android.synthetic.main.fragment_video_maker.changeFromCameraButton
import kotlinx.android.synthetic.main.fragment_video_maker.changeFromGalleryButton
import kotlinx.android.synthetic.main.fragment_video_maker.createButton
import kotlinx.android.synthetic.main.fragment_video_maker.exitButton
import kotlinx.android.synthetic.main.fragment_video_maker.pickFromGalleryButton
import kotlinx.android.synthetic.main.fragment_video_maker.sourceChooserGroup
import kotlinx.android.synthetic.main.fragment_video_maker.takeVideoButton
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.utill.*

class VideoMakerFragment : BaseFragment(R.layout.fragment_video_maker) {

    private val mViewModel by viewModel<VideoMakerViewModel> {
        parametersOf(getScope(R.id.nav_editor))
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
        initVideoView()
        setFullScreen()
        setOrientationPortrait()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setOrientationUnspecified()
        setFullScreen(false)
    }

    private fun initMenu() {
        createButton.setOnClickListener { mViewModel.onCreateDraft() }
        mViewModel.hasVideo.viewObserveWith { createButton.isVisible = it }
        exitButton.setOnClickListener { router().back() }
        mViewModel.eventExit { router().back() }
    }

    private fun initPickerControls() {
        takeVideoButton.setOnClickListener {}
        pickFromGalleryButton.setOnClickListener {
            mPickFromGalleryLauncher.launch(PICK_FROM_GALLERY_INPUT)
        }
        changeFromCameraButton.setOnClickListener { }
        changeFromGalleryButton.setOnClickListener {
            mPickFromGalleryLauncher.launch(PICK_FROM_GALLERY_INPUT)
        }

        mViewModel.hasVideo.viewObserveWith { hasPicture ->
            sourceChooserGroup.isVisible = !hasPicture
            changeFromCameraButton.isVisible = hasPicture
            changeFromGalleryButton.isVisible = hasPicture
        }
    }

    private fun initVideoView() {
        mViewModel.player.viewObserveNullableWith { player ->
            playerView.player = player
        }
    }

    companion object {
        private const val PICK_FROM_GALLERY_INPUT = "video/*"
    }

}
