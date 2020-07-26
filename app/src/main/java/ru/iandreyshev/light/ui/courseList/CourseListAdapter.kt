package ru.iandreyshev.light.ui.courseList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_timeline_image.view.title
import ru.iandreyshev.light.R
import ru.iandreyshev.light.domain.course.Course
import ru.iandreyshev.light.utill.safelyPosition

class CourseListAdapter(
    private val onClickListener: (position: Int) -> Unit
) : ListAdapter<Course, CourseViewHolder>(ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timeline_image, parent, false)
            .let { view -> CourseViewHolder(view) }
            .also { holder ->
                holder.itemView.setOnClickListener {
                    holder.safelyPosition { pos -> onClickListener(pos) }
                }
            }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.title.text = """
            Id: ${item.id}
            Title: ${item.title}
        """.trimIndent()
    }

}

class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view)

private object ItemCallback : DiffUtil.ItemCallback<Course>() {
    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean = false
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean = false
}
