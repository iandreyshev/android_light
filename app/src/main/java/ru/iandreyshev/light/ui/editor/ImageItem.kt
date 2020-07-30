package ru.iandreyshev.light.ui.editor

import android.view.View
import com.bumptech.glide.Glide
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_timeline_image.view.*
import ru.iandreyshev.light.R

class ImageItem(
    private val id: Long,
    private val imageName: String,
    private val imageUrl: String,
    private val onClickListener: () -> Unit
) : Item<ImageItem.ViewHolder>() {

    override fun getId() = id

    override fun getLayout() = R.layout.item_timeline_image

    override fun createViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.clickableArea.setOnClickListener { onClickListener() }
        viewHolder.itemView.subtitle.text = imageName

        Glide.with(viewHolder.itemView)
            .load(imageUrl)
            .centerCrop()
            .dontAnimate()
            .into(viewHolder.itemView.imagePreview)
    }

    override fun unbind(viewHolder: ViewHolder) {
        super.unbind(viewHolder)

        Glide.with(viewHolder.itemView)
            .clear(viewHolder.itemView.imagePreview)
    }

    class ViewHolder(view: View) : GroupieViewHolder(view)

}