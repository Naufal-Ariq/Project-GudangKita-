package com.example.gudangkita.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gudangkita.R

class ProductPickingAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductPickingAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvProductLocation: TextView = itemView.findViewById(R.id.tv_product_location)
        val ivStatusIcon: ImageView = itemView.findViewById(R.id.iv_status_icon)
        val imgProduct: ImageView = itemView.findViewById(R.id.img_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_product_picking_adapter, parent, false) // pastikan layout item
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.tvProductName.text = "${product.name} (${product.quantity} pcs)"
        holder.tvProductLocation.text = product.location

        // Load gambar produk menggunakan Glide
        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_placeholder_product) // gambar default jika kosong
            .into(holder.imgProduct)

        // Ubah ikon status
        if (product.isPicked) {
            holder.ivStatusIcon.setImageResource(R.drawable.ic_check_circle)
            holder.ivStatusIcon.imageTintList =
                holder.itemView.context.getColorStateList(R.color.status_green)
        } else {
            holder.ivStatusIcon.setImageResource(R.drawable.ic_circle_outline)
            holder.ivStatusIcon.imageTintList =
                holder.itemView.context.getColorStateList(R.color.status_gray)
        }
    }

    override fun getItemCount(): Int = productList.size
}