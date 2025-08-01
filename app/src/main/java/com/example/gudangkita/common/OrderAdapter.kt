package com.example.gudangkita.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gudangkita.R
import com.example.gudangkita.databinding.ItemOrderBinding
import com.example.gudangkita.common.ActivityLogger

class OrderAdapter(
    private var orders: List<Order>,
    private val onProsesClicked: (Order) -> Unit,
    private val onSelesaiClicked: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        val context = holder.itemView.context

        with(holder.binding) {
            // Set data order
            tvOrderId.text = "Order #${order.orderId}"
            tvTujuan.text = "Tujuan: ${order.tujuan}"

            // Atur status dan tampilannya
            tvStatus.text = order.status

            when (order.status) {
                "Menunggu" -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_pending))
                    tvStatus.setBackgroundResource(R.drawable.bg_status_pending)
                }
                "Diproses" -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_process))
                    tvStatus.setBackgroundResource(R.drawable.bg_status_processing)
                }
                "Selesai" -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_done))
                    tvStatus.setBackgroundResource(R.drawable.bg_status_completed)
                }
                else -> {
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.gray))
                    tvStatus.setBackgroundResource(R.drawable.bg_status_processing)
                }
            }

            // Tombol Proses hanya muncul saat status "Menunggu"
            btnProses.apply {
                visibility = if (order.status == "Menunggu") View.VISIBLE else View.GONE
                setOnClickListener {
                    onProsesClicked(order)
                    ActivityLogger.logActivity(
                        message = "Memproses pesanan #${order.orderId}",
                        iconResId = R.drawable.ic_process
                    )
                }
            }

            // Tombol Selesai hanya muncul saat status "Diproses"
            btnSelesai.apply {
                visibility = if (order.status == "Diproses") View.VISIBLE else View.GONE
                setOnClickListener {
                    onSelesaiClicked(order)
                    ActivityLogger.logActivity(
                        message = "Menyelesaikan pesanan #${order.orderId}",
                        iconResId = R.drawable.ic_check_circle
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateData(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}