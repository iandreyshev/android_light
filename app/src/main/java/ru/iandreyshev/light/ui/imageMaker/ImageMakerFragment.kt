package ru.iandreyshev.light.ui.imageMaker

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_image_maker.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.utill.setFullScreen
import ru.iandreyshev.light.utill.setOrientationPortrait
import ru.iandreyshev.light.utill.setOrientationUnspecified
import ru.iandreyshev.light.utill.uiLazy


class ImageMakerFragment : BaseFragment(R.layout.fragment_image_maker) {

    private val mViewModel by viewModel<ImageMakerViewModel> {
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
        initEditTextControl()
        initPickerControls()
        initPictureView()
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
        mViewModel.hasPicture.viewObserveWith { createButton.isVisible = it }
        exitButton.setOnClickListener { router().back() }
        mViewModel.eventExit { router().back() }
    }

    private fun initEditTextControl() {
        editTextButton.setOnClickListener {
            MaterialDialog(requireContext()).show {
                title(R.string.image_maker_text_title)
                input(
                    inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
                    maxLength = MAX_TEXT_LENGTH
                ) { _, text ->
                    mViewModel.onChangeText(text.toString())
                }
                negativeButton()
                lifecycleOwner(this@ImageMakerFragment)
            }
        }
        removeTextBalloonButton.setOnClickListener {
            mViewModel.onChangeText(null)
        }
        mViewModel.textBalloon.viewObserveWith { text ->
            textBalloon.isVisible = text.isNotBlank()
            balloonText.text = text
        }
        mViewModel.hasPicture.viewObserveWith { hasPicture ->
            editTextButton.isVisible = hasPicture
        }
    }

    private fun initPickerControls() {
        takePhotoButton.setOnClickListener {}
        pickFromGalleryButton.setOnClickListener {
            mPickFromGalleryLauncher.launch(PICK_FROM_GALLERY_INPUT)
        }
        changeFromCameraButton.setOnClickListener { }
        changeFromGalleryButton.setOnClickListener {
            mPickFromGalleryLauncher.launch(PICK_FROM_GALLERY_INPUT)
        }

        mViewModel.hasPicture.viewObserveWith { hasPicture ->
            pictureSourceChooserGroup.isVisible = !hasPicture
            changeFromCameraButton.isVisible = hasPicture
            changeFromGalleryButton.isVisible = hasPicture
        }
    }

    private fun initPictureView() {
        mViewModel.picture.viewObserveNullableWith { picture ->
            when (picture) {
                null ->
                    Glide.with(this)
                        .clear(imageView)
                else ->
                    Glide.with(this)
                        .load(picture)
                        .addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                mViewModel.onPictureLoadError(model)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean = false
                        })
                        .into(imageView)
            }
        }
    }

    companion object {
        private const val PICK_FROM_GALLERY_INPUT = "image/*"
        private const val MAX_TEXT_LENGTH = 500
    }

}
