package ru.iandreyshev.light.ui.editor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_timeline_image.view.*
import ru.iandreyshev.light.R
import ru.iandreyshev.light.utill.exhaustive

class TimelineAdapter : ListAdapter<TimelineItem, TimelineItemViewHolder>(ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineItemViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timeline_image, parent, false)
            .let { view ->
                TimelineItemViewHolder(view)
            }

    override fun onBindViewHolder(holder: TimelineItemViewHolder, position: Int) {
        when (getItem(position)) {
            is TimelineItem.Quiz -> {
                holder.itemView.title.text = """ 
                    Type: Quiz
                    Question: ???
                """.trimIndent()
            }
            is TimelineItem.Image -> {
                holder.itemView.title.text = """
                    Type: Image
                    Duration: ???
                    File: ???
                """.trimIndent()
            }
            is TimelineItem.Video -> {
                holder.itemView.title.text = """
                    Type: Video
                    Duration: ???
                    File: ???
                """.trimIndent()
            }
        }.exhaustive
    }

}

class TimelineItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

private object ItemCallback : DiffUtil.ItemCallback<TimelineItem>() {
    override fun areContentsTheSame(oldItem: TimelineItem, newItem: TimelineItem): Boolean = false
    override fun areItemsTheSame(oldItem: TimelineItem, newItem: TimelineItem): Boolean = false
}