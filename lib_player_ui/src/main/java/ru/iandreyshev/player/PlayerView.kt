package ru.iandreyshev.player

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.lay_player_image_view.view.*
import kotlinx.android.synthetic.main.lay_player_quiz_view.view.*
import kotlinx.android.synthetic.main.lay_player_video_view.view.*
import kotlinx.android.synthetic.main.view_player.view.*
import ru.iandreyshev.core_utils.exhaustive
import ru.iandreyshev.core_utils.uiLazy
import ru.iandreyshev.player.image.ImageViewViewController
import ru.iandreyshev.player.quiz.QuizViewViewController
import ru.iandreyshev.player.video.VideoViewViewController
import ru.iandreyshev.player_core.IWishListener
import ru.iandreyshev.player_core.course.PlayerItemState
import ru.iandreyshev.player_core.player.State
import ru.iandreyshev.player_core.quiz.QuizPlayerState

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.view_player, this)
        exitButton.setOnClickListener { mOnExitClickListener() }
    }

    private var mOnExitClickListener: () -> Unit = {}
    private var mWishListener: IWishListener = object : IWishListener {}

    private val mImageViewViewController by uiLazy {
        ImageViewViewController(
            view = imageView,
            onAction = { mWishListener(it.asWish()) }
        )
    }

    private val mQuizViewViewController by uiLazy {
        QuizViewViewController(
            view = quizView,
            onWish = mWishListener::invoke
        )
    }

    private val mVideoViewViewController by uiLazy {
        VideoViewViewController(
            view = videoView,
            onBack = { mWishListener(UiAction.Back.asWish()) },
            onForward = { mWishListener(UiAction.Forward.asWish()) }
        )
    }

    fun subscribe(listener: IWishListener) {
        mWishListener = listener
    }

    fun onExitClick(listener: () -> Unit) {
        mOnExitClickListener = listener
    }

    fun render(state: State) {
        when (state.type) {
            State.Type.PREPARE_PLAYER -> {
                playbackView.isVisible = false
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
            }
            State.Type.RESULT -> {
                preloadingProgressBar.isVisible = false
                playbackView.isVisible = false
                errorText.isVisible = false
                errorRepeatButton.isVisible = false

                resultView.isVisible = true
            }
            State.Type.PLAYING_ITEM_ERROR,
            State.Type.PREPARE_PLAYER_ERROR -> {
                preloadingProgressBar.isVisible = false
                playbackView.isVisible = false
                resultView.isVisible = false

                errorText.isVisible = true
                errorText.text = state.error
                errorRepeatButton.isVisible = true
                errorRepeatButton.setOnClickListener {
                    mWishListener(UiAction.Repeat.asWish())
                }
            }
        }.exhaustive

        renderItemState(state.itemState)
    }

    fun render(state: QuizPlayerState) {
        mQuizViewViewController.render(state)
    }

    fun pause() {
        mVideoViewViewController.pause()
    }

    private fun renderItemState(state: PlayerItemState?) {
        when (state) {
            is PlayerItemState.Image -> {
                mVideoViewViewController.render(null)
                mImageViewViewController.render(state)
            }
            is PlayerItemState.Video -> {
                mImageViewViewController.render(null)
                mVideoViewViewController.render(state)
            }
            else -> {
                mImageViewViewController.render(null)
                mVideoViewViewController.render(null)
            }
        }.exhaustive
    }

}
