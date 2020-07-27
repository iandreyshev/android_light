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
import ru.iandreyshev.light.ui.player.mvi.News
import ru.iandreyshev.light.ui.player.mvi.State

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
    }

    private fun render(state: State) {
        when (state.playbackItem) {
            null -> {
                preloadingProgressBar.isVisible = false
            }
            else -> {
                preloadingProgressBar.isVisible = true
                // TODO: Render item
            }
        }

        when (val playback = state.playback) {
            null -> {
                playbackView.isVisible = false
            }
            else -> {
                playbackView.isVisible = true
                playbackView.text = "${playback.itemNumber} / " +
                        "${state.playbackItemsCount} " +
                        "(${playback.itemProgress})"
            }
        }
    }

    private fun renderPlaybackView(state: CoursePlaybackState?) {
        playbackView.isVisible = state != null
        state ?: return


    }

}