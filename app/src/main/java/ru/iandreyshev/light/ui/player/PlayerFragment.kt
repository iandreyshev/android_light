package ru.iandreyshev.light.ui.player

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_image_maker.*
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.fragment_player.exitButton
import kotlinx.android.synthetic.main.fragment_player.imageView
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.iandreyshev.light.BaseFragment
import ru.iandreyshev.light.R
import ru.iandreyshev.light.domain.player.ItemState
import ru.iandreyshev.light.navigation.router
import ru.iandreyshev.light.ui.player.mvi.News
import ru.iandreyshev.light.ui.player.mvi.State
import ru.iandreyshev.light.utill.exhaustive
import ru.iandreyshev.light.utill.setFullScreen
import ru.iandreyshev.light.utill.setOrientationPortrait
import ru.iandreyshev.light.utill.setOrientationUnspecified

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
        exitButton.setOnClickListener { router().back() }

        setFullScreen()
        setOrientationPortrait()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setOrientationUnspecified()
        setFullScreen(false)
    }

    private fun render(state: State) {
        when (state.type) {
            State.Type.PREPARE_PLAYER -> {
                playbackView.isVisible = false
                imageViewProgressBar.isVisible = false
                imageView.isVisible = false
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
                imageViewProgressBar.isVisible = false
                imageView.isVisible = false
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
                imageViewProgressBar.isVisible = false
                imageView.isVisible = false
                resultView.isVisible = false

                errorText.isVisible = true
                errorText.text = state.error
                errorRepeatButton.isVisible = true
                errorRepeatButton.setOnClickListener { mViewModel(UiAction.Repeat) }
            }
        }.exhaustive
    }

    private fun renderItemState(state: ItemState?) {
        when (state) {
            is ItemState.Image -> {
                quizRecyclerView.isVisible = false

                imageViewProgressBar.isVisible = true
                Glide.with(this)
                    .load(state.uri)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            imageViewProgressBar.isVisible = false
                            mViewModel(UiAction.LoadImageError)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            imageViewProgressBar.isVisible = false
                            return false
                        }
                    })
                    .into(imageView)

                Unit
            }
            is ItemState.Quiz -> {
                imageView.isVisible = false
                imageViewProgressBar.isVisible = false

                quizRecyclerView.isVisible = true
            }
            null -> Unit
        }.exhaustive
    }

}
