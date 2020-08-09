package ru.iandreyshev.player.image

import android.graphics.drawable.Drawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.lay_player_image_view.view.*
import ru.iandreyshev.player.UiAction
import ru.iandreyshev.player_core.course.PlayerItemState

internal class ImageViewViewController(
    private val view: ConstraintLayout,
    private val onAction: (UiAction) -> Unit
) {

    init {
        view.forwardButton.setOnClickListener { onAction(UiAction.Forward) }
        view.backButton.setOnClickListener { onAction(UiAction.Back) }
    }

    fun render(state: PlayerItemState.Image?) {
        state ?: kotlin.run {
            view.isVisible = false
            return
        }

        view.isVisible = true
        view.imageViewProgressBar.isVisible = true

        Glide.with(view.imageViewForImage)
            .load(state.uri)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.imageViewProgressBar.isVisible = false
                    onAction(UiAction.LoadImageError)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.imageViewProgressBar.isVisible = false
                    return false
                }
            })
            .into(view.imageViewForImage)
    }

}
