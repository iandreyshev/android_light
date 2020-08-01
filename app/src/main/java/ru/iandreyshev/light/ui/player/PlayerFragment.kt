package ru.iandreyshev.light.ui.player

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.fragment_player.exitButton
import kotlinx.android.synthetic.main.lay_image_view.*
import kotlinx.android.synthetic.main.lay_quiz_view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.ui.player.image.ImageViewViewController
import ru.iandreyshev.light.ui.player.mvi.News
import ru.iandreyshev.light.ui.player.mvi.State
import ru.iandreyshev.light.ui.player.quiz.QuizViewViewController
import ru.iandreyshev.light.utill.*

class PlayerFragment : BaseFragment(R.layout.fragment_player) {

    private val mArgs: PlayerFragmentArgs by navArgs()
    private val mViewModel by viewModel<PlayerViewModel> {
        parametersOf(getScope(R.id.nav_player), mArgs.playerArgs)
    }

    private val mImageViewViewController by uiLazy {
        ImageViewViewController(
            imageView,
            mViewModel::invoke
        )
    }
    private val mQuizViewViewController by uiLazy { QuizViewViewController(quizView) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.state.viewObserveWith(::render)
        mViewModel.eventShowNews { news ->
            when (news) {
                is News.ToastNews ->
                    Toast.makeText(requireContext(), news.text, Toast.LENGTH_SHORT).show()
            }
        }

        exitButton.setOnClickListener { router().back() }

        setFullScreen()
        setOrientationPortrait()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mQuizViewViewController.dispose()
        setOrientationUnspecified()
        setFullScreen(false)
    }

    private fun render(state: State) {
        when (state.type) {
            State.Type.PREPARE_PLAYER -> {
                playbackView.isVisible = false
                mImageViewViewController.hide()
                mQuizViewViewController.hide()
                resultView.isVisible = false
                errorText.isVisible = false
                errorRepeatButton.isVisible = false

                preloadingProgressBar.isVisible = true
            }
            State.Type.PLAYING_ITEM -> {
                preloadingProgressBar.isVisible = false
                resultView.isVisible = false
                errorText.isVisible = false
                errorRepeatButton.isVisible = false

                playbackView.isVisible = true
                playbackView.text = "${state.itemPosition + 1} / " +
                        "${state.itemsCount} "

                renderItemState(state.itemState)
            }
            State.Type.RESULT -> {
                preloadingProgressBar.isVisible = false
                mImageViewViewController.hide()
                mQuizViewViewController.hide()
                playbackView.isVisible = false
                errorText.isVisible = false
                errorRepeatButton.isVisible = false

                resultView.isVisible = true
                resultView.text = state.result
            }
            State.Type.PLAYING_ITEM_ERROR,
            State.Type.PREPARE_PLAYER_ERROR -> {
                preloadingProgressBar.isVisible = false
                playbackView.isVisible = false
                mImageViewViewController.hide()
                mQuizViewViewController.hide()
                resultView.isVisible = false

                errorText.isVisible = true
                errorText.text = state.error
                errorRepeatButton.isVisible = true
                errorRepeatButton.setOnClickListener { mViewModel(UiAction.Repeat) }
            }
        }.exhaustive
    }

    private fun renderItemState(state: CourseItemState?) {
        when (state) {
            is CourseItemState.Image -> {
                mQuizViewViewController.hide()
                mImageViewViewController.update(state)
            }
            is CourseItemState.Quiz -> {
                mImageViewViewController.hide()
                mQuizViewViewController.update(state)
            }
            null -> Unit
        }.exhaustive
    }

}
