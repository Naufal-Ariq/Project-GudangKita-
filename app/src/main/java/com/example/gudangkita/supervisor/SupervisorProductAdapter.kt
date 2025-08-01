package com.example.gudangkita.supervisor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gudangkita.common.Product
import com.example.gudangkita.R

class SupervisorProductAdapter(
    private var products: List<Product>,
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<SupervisorProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name_supervisor)
        val tvProductDetails: TextView = itemView.findViewById(R.id.tv_product_details_supervisor)
        val btnEdit: TextView = itemView.findViewById(R.id.btn_edit_product)
        val btnDelete: TextView = itemView.findViewById(R.id.btn_delete_product)

        val imgProduct: ImageView = itemView.findViewById(R.id.img_product)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_supervisor_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvProductName.text = product.name
        holder.tvProductDetails.text = "SKU: ${product.sku} | Stok: ${product.quantity}"

        val context = holder.itemView.context
        val lowStock = product.quantity < 10
        val nameColor = if (lowStock) R.color.red_500 else R.color.gray_800
        val detailColor = if (lowStock) R.color.red_500 else R.color.gray_600

        holder.tvProductName.setTextColor(ContextCompat.getColor(context, nameColor))
        holder.tvProductDetails.setTextColor(ContextCompat.getColor(context, detailColor))

        // Tambahkan aksi edit dan delete
        holder.btnEdit.setOnClickListener { onEditClick(product) }
        holder.btnDelete.setOnClickListener { onDeleteClick(product) }

        Glide.with(holder.itemView.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_placeholder_product)
            .error(R.drawable.ic_placeholder_product)
            .into(holder.imgProduct)

    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<Product>) {
        this.products = newProducts
        notifyDataSetChanged()
    }
}