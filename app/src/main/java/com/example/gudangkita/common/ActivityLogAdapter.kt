package com.example.gudangkita.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gudangkita.R
import java.text.SimpleDateFormat
import java.util.Locale

class ActivityLogAdapter(private var logs: List<ActivityLog>) :
    RecyclerView.Adapter<ActivityLogAdapter.LogViewHolder>() {

    inner class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.iv_log_icon)
        val message: TextView = itemView.findViewById(R.id.tv_log_description)
        val timestamp: TextView = itemView.findViewById(R.id.tv_log_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity_log, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]
        val context = holder.itemView.context

        // Set pesan log
        holder.message.text = log.message.ifEmpty { "Aktivitas tidak diketahui" }

        // Format timestamp
        val formattedTime = log.timestamp?.let {
            val date = it.toDate()
            SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(date)
        } ?: "Waktu tidak diketahui"

        holder.timestamp.text = formattedTime // ✅ Tampilkan waktu ke TextView

        // Set ikon & warna sesuai iconResId
        when (log.iconResId) {
            R.drawable.ic_process -> {
                holder.icon.setImageResource(R.drawable.ic_process)
                holder.icon.imageTintList = ContextCompat.getColorStateList(context, R.color.status_yellow)
            }
            R.drawable.ic_check_circle -> {
                holder.icon.setImageResource(R.drawable.ic_check_circle)
                holder.icon.imageTintList = ContextCompat.getColorStateList(context, R.color.status_green)
            }
            R.drawable.ic_warning -> {
                holder.icon.setImageResource(R.drawable.ic_warning)
                holder.icon.imageTintList = ContextCompat.getColorStateList(context, R.color.status_red)
            }
            else -> {
                holder.icon.setImageResource(R.drawable.ic_info)
                holder.icon.imageTintList = ContextCompat.getColorStateList(context, R.color.gray_600)
            }
        }
    }

    override fun getItemCount(): Int = logs.size

    fun updateLogs(newLogs: List<ActivityLog>) {
        logs = newLogs
        notifyDataSetChanged()
    }
}