package ru.iandreyshev.light.ui.player

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_player.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.domain.course.CourseItem
import ru.iandreyshev.light.ui.player.mvi.News
import ru.iandreyshev.light.ui.player.mvi.State
import ru.iandreyshev.light.utill.exhaustive

class PlayerFragment : BaseFragment(R.layout.fragment_player) {

    private val mArgs: PlayerFragmentArgs by navArgs()

    private val mViewModel by viewModel<PlayerViewModel> {
        parametersOf(getScope(R.id.nav_player), mArgs.playerArgs)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.state.viewObserveWith(::render)
        mViewModel.eventShowNews { news ->
            when (news) {
                is News.ToastNews ->
                    Toast.makeText(requireContext(), news.text, Toast.LENGTH_SHORT).show()
            }
        }

        forwardButton.setOnClickListener { mViewModel(UiAction.Forward) }
        backButton.setOnClickListener { mViewModel(UiAction.Back) }
    }

    private fun render(state: State) {
        when (state.type) {
            State.Type.PREPARE_PLAYER -> {
                playbackView.isVisible = false
                currentView.isVisible = false
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
                playbackView.text = "${state.courseItemPosition + 1} / " +
                        "${state.courseItemsCount} "
                currentView.isVisible = true

                when (state.courseItem) {
                    is CourseItem.Image -> {
                        currentView.text = "CourseItem: Image"
                    }
                    is CourseItem.Quiz -> {
                        currentView.text = "CourseItem: Quiz"
                    }
                    is CourseItem.Video -> {
                        currentView.text = "CourseItem: Video"
                    }
                    null -> Unit
                }.exhaustive
            }
            State.Type.RESULT -> {
                preloadingProgressBar.isVisible = false
                playbackView.isVisible = false
                currentView.isVisible = false
                errorText.isVisible = false
                errorRepeatButton.isVisible = false

                resultView.isVisible = true
                resultView.text = state.result
            }
            State.Type.PLAYING_ITEM_ERROR,
            State.Type.PREPARE_PLAYER_ERROR -> {
                preloadingProgressBar.isVisible = false
                playbackView.isVisible = false
                currentView.isVisible = false
                resultView.isVisible = false

                errorText.isVisible = true
                errorText.text = state.error
                errorRepeatButton.isVisible = true
                errorRepeatButton.setOnClickListener { mViewModel(UiAction.Repeat) }
            }
        }.exhaustive
    }

}
