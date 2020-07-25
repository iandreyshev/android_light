package ru.iandreyshev.light.ui.imageMaker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.dialog_image_maker_edit_text.view.*
import kotlinx.android.synthetic.main.fragment_image_maker.*
import kotlinx.android.synthetic.main.fragment_quiz_maker.toolbar
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.domain.imageMaker.ImageDuration
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.utill.dismissOnDestroy
import ru.iandreyshev.light.utill.withItemListeners

class ImageMakerFragment : BaseFragment(R.layout.fragment_image_maker) {

    private val mViewModel by viewModel<ImageMakerViewModel> {
        parametersOf(getScope(R.id.nav_editor))
    }
    private var mAlertDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initEditTextControl()
        initDurationControl()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAlertDialog.dismissOnDestroy()
    }

    private fun initMenu() {
        toolbar.setNavigationOnClickListener { router().back() }
        toolbar.withItemListeners {
            R.id.actionImageMakerSave { mViewModel.onSave() }
        }
        mViewModel.eventExit { router().back() }
    }

    private fun initEditTextControl() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_image_maker_edit_text, null)
        editTextButton.setOnClickListener {
            mAlertDialog.dismissOnDestroy()
            mAlertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton(R.string.common_dialog_ok) { _, _ ->
                    val text = dialogView.textInput.text
                    mViewModel.onChangeText(text.toString())
                }
                .setNegativeButton(R.string.common_dialog_cancel) { _, _ ->
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
    }

    private fun initDurationControl() {
        durationButton.setOnClickListener {
            mViewModel.onSwitchDuration()
        }
        mViewModel.duration.viewObserveWith {
            durationText.text = when (it) {
                ImageDuration.SEC_3 -> "3s"
                ImageDuration.SEC_5 -> "5s"
                ImageDuration.SEC_10 -> "10s"
            }
        }
    }

}
