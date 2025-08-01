package com.example.gudangkita.staff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gudangkita.R

class TaskItemAdapter(
    private val items: List<TaskItem>
) : RecyclerView.Adapter<TaskItemAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_item_location)
        val imgStatus: ImageView = itemView.findViewById(R.id.img_item_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_staf_task_detail, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvLocation.text = item.location

        if (item.isScanned) {
            holder.imgStatus.setImageResource(R.drawable.ic_check_circle)
        } else {
            holder.imgStatus.setImageResource(R.drawable.ic_circle_unchecked)
        }
    }

    override fun getItemCount(): Int = items.size
}