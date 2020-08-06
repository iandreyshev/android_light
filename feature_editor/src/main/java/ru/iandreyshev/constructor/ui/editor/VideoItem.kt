package ru.iandreyshev.constructor.ui.editor

import android.view.View
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_timeline_video.view.*
import ru.iandreyshev.constructor.R

class VideoItem(
    private val id: Long,
    private val videoName: String,
    private val duration: String,
    private val onClickListener: () -> Unit
) : Item<VideoItem.ViewHolder>() {

    override fun getId() = id

    override fun getLayout() = R.layout.item_timeline_video

    override fun createViewHolder(itemView: View) = ViewHolder(itemView)

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.clickableArea.setOnClickListener { onClickListener() }
        viewHolder.itemView.subtitle.text = duration
        viewHolder.itemView.subtitleSecond.text = videoName
    }

    class ViewHolder(view: View) : GroupieViewHolder(view)

}