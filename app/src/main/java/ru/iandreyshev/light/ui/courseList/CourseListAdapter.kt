package ru.iandreyshev.light.ui.courseList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_course_list_course.view.*
import ru.iandreyshev.core_ui.safelyPosition
import ru.iandreyshev.light.R
import ru.iandreyshev.player_core.course.Course

class CourseListAdapter(
    private val onClickListener: (position: Int) -> Unit,
    private val onLongClickListener: (position: Int) -> Unit
) : ListAdapter<Course, CourseViewHolder>(ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_list_course, parent, false)
            .let { view -> CourseViewHolder(view) }
            .also { holder ->
                holder.itemView.setOnClickListener {
                    holder.safelyPosition(onClickListener)
                }
                holder.itemView.setOnLongClickListener {
                    holder.safelyPosition(onLongClickListener)
                    return@setOnLongClickListener true
                }
            }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.title.text = item.title
        holder.itemView.creationDate.text = item.creationDate.toString()
    }

}

class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view)

private object ItemCallback : DiffUtil.ItemCallback<Course>() {
    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean = false
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean = false
}
