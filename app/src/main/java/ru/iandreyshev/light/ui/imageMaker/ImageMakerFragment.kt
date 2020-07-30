package ru.iandreyshev.light.ui.imageMaker

import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.dialog_image_maker_edit_text.view.*
import kotlinx.android.synthetic.main.fragment_image_maker.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.utill.dismissOnDestroy
import ru.iandreyshev.light.utill.uiLazy

class ImageMakerFragment : BaseFragment(R.layout.fragment_image_maker) {

    private val mViewModel by viewModel<ImageMakerViewModel> {
        parametersOf(getScope(R.id.nav_editor))
    }
    private var mAlertDialog: AlertDialog? = null
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

        activity?.window?.addFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onDestroyView() {
        super.onDestroyView()

        activity?.window?.clearFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onDestroy() {
        super.onDestroy()
        mAlertDialog.dismissOnDestroy()
    }

    private fun initMenu() {
        createButton.setOnClickListener { mViewModel.onCreateDraft() }
        mViewModel.hasPicture.viewObserveWith { createButton.isVisible = it }
        exitButton.setOnClickListener { router().back() }
        mViewModel.eventExit { router().back() }
    }

    private fun initEditTextControl() {
        editTextButton.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_image_maker_edit_text, null)

            mAlertDialog.dismissOnDestroy()
            mAlertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton(R.string.common_dialog_ok) { _, _ ->
                    val text = dialogView.textInput.text
                    mViewModel.onChangeText(text.toString())
                    mAlertDialog.dismissOnDestroy()
                }
                .setNegativeButton(R.string.common_dialog_cancel) { _, _ ->
                    mAlertDialog.dismissOnDestroy()
                }
                .setOnCancelListener {
                    mAlertDialog.dismissOnDestroy()
                }
                .show()
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
                        .centerCrop()
                        .dontAnimate()
                        .addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                mViewModel.onPictureLoadCompleted(model, false)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                mViewModel.onPictureLoadCompleted(model, true)
                                return false
                            }
                        })
                        .into(imageView)
            }
        }
    }

    companion object {
        private const val PICK_FROM_GALLERY_INPUT = "image/*"
    }

}
