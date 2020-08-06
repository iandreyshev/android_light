package ru.iandreyshev.constructor.ui.imageMaker

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_image_maker.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.constructor.R
import ru.iandreyshev.constructor.navigation.router
import ru.iandreyshev.core_app.BaseFragment
import ru.iandreyshev.core_ui.setFullScreen
import ru.iandreyshev.core_ui.setOrientationPortrait
import ru.iandreyshev.core_ui.setOrientationUnspecified
import ru.iandreyshev.core_utils.exhaustive
import ru.iandreyshev.core_utils.uiLazy
import java.io.File

class ImageMakerFragment : BaseFragment(R.layout.fragment_image_maker) {

    private val mViewModel by viewModel<ImageMakerViewModel> {
        val navArgs by navArgs<ImageMakerFragmentArgs>()
        parametersOf(getScope(R.id.nav_editor), navArgs.imageMakerArgs)
    }
    private val mPickFromGalleryLauncher by uiLazy {
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            mViewModel.onPickFromGallery(it.toString())
        }
    }
    private val mRequestCameraPermissionLauncher by uiLazy {
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            mViewModel::onCameraPermissionGranted
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initEditTextControl()
        initPickerControls()
        initPictureView()
        initCameraView()
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
        pickFromGalleryButton.setOnClickListener {
            mPickFromGalleryLauncher.launch(PICK_FROM_GALLERY_INPUT)
        }
        changeFromCameraButton.setOnClickListener { }
        changeFromGalleryButton.setOnClickListener {
            mPickFromGalleryLauncher.launch(PICK_FROM_GALLERY_INPUT)
        }

        mViewModel.hasPicture.viewObserveWith { hasPicture ->
            sourceChooserGroup.isVisible = !hasPicture
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

    private fun initCameraView() {
        mRequestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)

        mViewModel.eventTakePhoto(::onTakePhoto)
        mViewModel.cameraAvailability.viewObserveWith {
            when (it) {
                CameraState.AWAIT_PERMISSION -> {
                    cameraView.isVisible = false
                    needCameraPermissionText.isVisible = false
                    needCameraPermissionButton.isVisible = false
                }
                CameraState.DISABLED -> {
                    cameraView.isVisible = false
                    needCameraPermissionText.isVisible = true
                    needCameraPermissionButton.isVisible = true
                    needCameraPermissionButton.setOnClickListener {
                        mRequestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
                CameraState.AVAILABLE -> {
                    cameraView.isVisible = true
                    needCameraPermissionText.isVisible = false
                    needCameraPermissionButton.isVisible = false

                    val hasPermission = ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    )
                    if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                        mViewModel.onCameraPermissionGranted(false)
                        return@viewObserveWith
                    }

                    cameraView.bindToLifecycle(this)
                    takePhotoButton.setOnClickListener { mViewModel.onTakePhotoClick() }
                }
            }.exhaustive
        }
    }

    private fun onTakePhoto(filePath: String) {
        val outputFile = File(filePath)
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(outputFile)
            .build()

        cameraView.takePicture(
            outputOptions,
            Dispatchers.IO.asExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    lifecycleScope.launch {
                        mViewModel.onTakePhotoSuccess(filePath)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    lifecycleScope.launch {
                        mViewModel.onTakePhotoError(exception)
                    }
                }
            })
    }

    companion object {
        private const val PICK_FROM_GALLERY_INPUT = "image/*"
        private const val MAX_TEXT_LENGTH = 500
    }

}
