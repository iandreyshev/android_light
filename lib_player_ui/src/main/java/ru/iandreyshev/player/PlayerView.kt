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
import ru.iandreyshev.player_core.player.State
import ru.iandreyshev.player.quiz.QuizViewViewController
import ru.iandreyshev.player_core.quiz.Wish
import ru.iandreyshev.player.video.VideoViewViewController
import ru.iandreyshev.player_core.course.PlayerItemState
import ru.iandreyshev.player_core.player.PlayerWish
import ru.iandreyshev.player_core.quiz.QuizPlayerState
import ru.iandreyshev.player_core.video.VideoPlayerState

class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.view_player, this)
    }

    private var mOnExitClickListener: () -> Unit = {}
    private var mPlayerWishListener: (PlayerWish) -> Unit = {}
    private var mQuizPlayerWishListener: (Wish) -> Unit = {}

    fun onPlayerWish(listener: (PlayerWish) -> Unit) {
        mPlayerWishListener = listener
    }

    fun onQuizPlayerWish(listener: (Wish) -> Unit) {
        mQuizPlayerWishListener = listener
    }

    fun onExitClick(listener: () -> Unit) {
        mOnExitClickListener = listener
    }

    private val mImageViewViewController by uiLazy {
        ImageViewViewController(
            view = imageView,
            onAction = { mPlayerWishListener(it.asWish()) }
        )
    }

    private val mQuizViewViewController by uiLazy {
        QuizViewViewController(
            view = quizView,
            onWish = mQuizPlayerWishListener
        )
    }

    private val mVideoViewViewController by uiLazy {
        VideoViewViewController(
            view = videoView,
            onBack = { mPlayerWishListener(UiAction.Back.asWish()) },
            onForward = { mPlayerWishListener(UiAction.Forward.asWish()) }
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        exitButton.setOnClickListener { mOnExitClickListener() }
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
                resultView.text = state.result
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
                    mPlayerWishListener(UiAction.Repeat.asWish())
                }
            }
        }.exhaustive

        renderItemState(state.itemState)
    }

    fun render(state: QuizPlayerState) {
        mQuizViewViewController.render(state)
    }

    fun render(state: VideoPlayerState) {
        mVideoViewViewController.render(state.player)
    }

    private fun renderItemState(state: PlayerItemState?) {
        when (state) {
            is PlayerItemState.Image -> {
//                mVideoPlayerViewModel.hide()
                mImageViewViewController.render(state)
            }
            is PlayerItemState.Video -> {
                mImageViewViewController.hide()
//                mVideoPlayerViewModel.render(state)
            }
            else -> {
//                mVideoPlayerViewModel.hide()
                mImageViewViewController.hide()
            }
        }.exhaustive
    }


}
