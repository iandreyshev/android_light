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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dev.chrisbanes.insetter.applySystemGestureInsetsToMargin
import dev.chrisbanes.insetter.applySystemWindowInsetsToMargin
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import kotlinx.android.synthetic.main.fragment_image_maker.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.constructor.R
import ru.iandreyshev.constructor.domain.image.ImageSource
import ru.iandreyshev.constructor.navigation.router
import ru.iandreyshev.core_app.BaseFragment
import ru.iandreyshev.core_ui.toast
import ru.iandreyshev.core_utils.exhaustive
import ru.iandreyshev.core_utils.uiLazy
import java.io.File

class ImageMakerFragment : BaseFragment(R.layout.fragment_image_maker) {

    private val mViewModel by viewModel<ImageMakerViewModel> {
        val navArgs by navArgs<ImageMakerFragmentArgs>()
        parametersOf(getScope(R.id.nav_editor), navArgs.imageMakerArgs)
    }
    private val mPickFromGalleryLauncher by uiLazy {
        registerForActivityResult(
            ActivityResultContracts.GetContent(),
            mViewModel::onPickFromGallerySuccess
        )
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
        initPickerControls()
        initDescriptionView()
        initCameraView()

        mViewModel.state.viewObserveWith(::render)
        mViewModel.event(::handleEvent)
    }

    private fun initMenu() {
        createButton.applySystemGestureInsetsToMargin(top = true)
        createButton.setOnClickListener { mViewModel.onSaveDraft() }
        exitButton.applySystemWindowInsetsToMargin(top = true)
        exitButton.setOnClickListener { mViewModel.onExit() }
    }

    private fun initPickerControls() {
        sourceChooserGroup.applySystemWindowInsetsToMargin(bottom = true)

        pickFromGalleryButton.setOnClickListener {
            mPickFromGalleryLauncher.launch(PICK_FROM_GALLERY_INPUT)
        }
        changeFromCameraButton.setOnClickListener {
            mViewModel.onOpenCameraClick()
        }
        changeFromGalleryButton.setOnClickListener {
            mPickFromGalleryLauncher.launch(PICK_FROM_GALLERY_INPUT)
        }
    }

    private fun initDescriptionView() {
        descriptionView.applySystemWindowInsetsToPadding(bottom = true)
    }

    private fun initCameraView() {
        mRequestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun handleEvent(event: ImageMakerEvent) {
        when (event) {
            is ImageMakerEvent.ShowError -> toast(event.text)
            is ImageMakerEvent.TakePhoto -> takePhoto(event.imageSource)
            ImageMakerEvent.Exit -> router.back()
        }.exhaustive
    }

    private fun takePhoto(source: ImageSource) {
        val outputFile = File(source.filePath)
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(outputFile)
            .build()

        cameraView.takePicture(
            outputOptions,
            Dispatchers.IO.asExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    lifecycleScope.launch {
                        mViewModel.onTakePhotoSuccess(source)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    lifecycleScope.launch {
                        mViewModel.onTakePhotoError(exception)
                    }
                }
            })
    }

    private fun render(state: ImageMakerViewState) {
        createButton.isVisible = state.hasPicture
        editDescriptionButton.isVisible = state.hasPicture

        sourceChooserGroup.isVisible = !state.hasPicture
        changeFromCameraButton.isVisible = state.hasPicture
        changeFromGalleryButton.isVisible = state.hasPicture

        descriptionView.isVisible = state.hasText
        descriptionText.text = state.text

        renderEditTextDialog(state.text)
        renderPicture(state.picture)
        renderCameraView(state.cameraState)
    }

    private fun renderEditTextDialog(text: String) {
        editDescriptionButton.setOnClickListener {
            MaterialDialog(requireContext()).show {
                var newText = ""
                title(R.string.image_maker_text_title)
                input(
                    prefill = text,
                    inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
                    maxLength = MAX_TEXT_LENGTH
                ) { _, text ->
                    newText = text.toString()
                }
                positiveButton(text = "Save") {
                    mViewModel.onChangeText(newText)
                }
                neutralButton(text = "Delete") {
                    mViewModel.onChangeText(null)
                }
                negativeButton(text = "Cancel")
                lifecycleOwner(this@ImageMakerFragment)
            }
        }
    }

    private fun renderPicture(picture: String?) {
        when (picture) {
            null ->
                Glide.with(this)
                    .clear(imageView)
            else ->
                Glide.with(this)
                    .load(picture)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            mViewModel.onPictureLoadError(e)
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

    private fun renderCameraView(state: CameraState) {
        when (state) {
            CameraState.AWAIT_PERMISSION -> {
                cameraView.isVisible = false
                needCameraPermissionText.isVisible = true
                needCameraPermissionButton.isVisible = true
                needCameraPermissionButton.setOnClickListener {
                    mRequestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
            CameraState.DISABLED -> {
                cameraView.isVisible = false
                needCameraPermissionText.isVisible = false
                needCameraPermissionButton.isVisible = false
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
                    return
                }

                cameraView.bindToLifecycle(this)
                takePhotoButton.setOnClickListener { mViewModel.onTakePhotoClick() }
            }
        }.exhaustive
    }

    companion object {
        private const val PICK_FROM_GALLERY_INPUT = "image/*"
        private const val MAX_TEXT_LENGTH = 500
    }

}
