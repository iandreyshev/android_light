package ru.iandreyshev.player

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
        exitClickableArea.setOnClickListener { mWishListener(UiAction.Exit.asWish()) }
        playbackProgressBar.progressColor =
            ContextCompat.getColor(context, R.color.green)
        playbackProgressBar.progressBackgroundColor =
            ContextCompat.getColor(context, R.color.gray_dark)
    }

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
            onWish = { mWishListener(it) }
        )
    }

    private val mVideoViewViewController by uiLazy {
        VideoViewViewController(
            view = videoView,
            onBack = { mWishListener(UiAction.Back.asWish()) },
            onForward = { mWishListener(UiAction.Forward.asWish()) }
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        mQuizViewViewController.setQuizViewTopMargin(
            playbackView.measuredHeight + resources.getDimensionPixelSize(R.dimen.grid_step_2)
        )
    }

    fun subscribe(listener: IWishListener) {
        mWishListener = listener
    }

    fun render(state: State) {
        when (state.type) {
            State.Type.PREPARE_PLAYER -> {
                playbackView.isVisible = false
                resultView.isVisible = false
                errorText.isVisible = false
                errorRepeatButton.isVisible = false

                preloadingProgressBar.isVisible = true
                exitButton.isVisible = true
            }
            State.Type.PLAYING_ITEM -> {
                preloadingProgressBar.isVisible = false
                resultView.isVisible = false
                errorText.isVisible = false
                errorRepeatButton.isVisible = false

                playbackView.isVisible = true
                playbackText.text = resources.getString(
                    R.string.playback_items_count,
                    state.itemPosition + 1,
                    state.itemsCount
                )
                playbackProgressBar.max = state.itemsCount
                playbackProgressBar.progress = state.itemPosition + 1
                exitButton.isVisible = true
            }
            State.Type.RESULT -> {
                preloadingProgressBar.isVisible = false
                playbackView.isVisible = false
                errorText.isVisible = false
                errorRepeatButton.isVisible = false
                exitButton.isVisible = false

                resultView.isVisible = true
                finishButton.setOnClickListener {
                    mWishListener(UiAction.Exit.asWish())
                }
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
                exitButton.isVisible = true
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
