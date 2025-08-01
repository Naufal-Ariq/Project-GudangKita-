package com.example.gudangkita.staff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gudangkita.R

data class StafOrder(
    val orderId: String,
    val destination: String,
    val items: String,
    val status: String
)

class StafOrderAdapter(
    private val orders: List<StafOrder>,
    private val onClick: (StafOrder) -> Unit
) : RecyclerView.Adapter<StafOrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tv_order_id)
        val tvOrderDestination: TextView = itemView.findViewById(R.id.tv_order_destination)
        val tvOrderItems: TextView = itemView.findViewById(R.id.tv_order_items)
        val tvOrderStatus: TextView = itemView.findViewById(R.id.tv_order_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_staf_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.tvOrderId.text = "Pesanan ${order.orderId}"
        holder.tvOrderDestination.text = "Tujuan: ${order.destination}"
        holder.tvOrderItems.text = order.items
        holder.tvOrderStatus.text = order.status

        // Ganti warna status sesuai kondisi
        when (order.status) {
            "Diproses" -> holder.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_chip)
            "Selesai" -> holder.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_chip_green)
            else -> holder.tvOrderStatus.setBackgroundResource(R.drawable.bg_status_chip_gray)
        }

        holder.itemView.setOnClickListener { onClick(order) }
    }

    override fun getItemCount(): Int = orders.size
}
