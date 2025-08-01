package com.example.gudangkita.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gudangkita.R
import com.example.gudangkita.staff.OrderItem

class OrderItemAdapter(
    private val items: List<OrderItem>
) : RecyclerView.Adapter<OrderItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvQty: TextView = itemView.findViewById(R.id.tv_item_qty)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_item_location)
        val imgStatus: ImageView = itemView.findViewById(R.id.img_item_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_staf_task_detail, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name
        holder.tvQty.text = "${item.qty} pcs"
        holder.tvLocation.text = item.location
        holder.imgStatus.setImageResource(R.drawable.ic_circle_unchecked)
    }

    override fun getItemCount(): Int = items.size
}
