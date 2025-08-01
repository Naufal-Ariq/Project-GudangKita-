package com.example.gudangkita.staff

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gudangkita.common.User
import com.example.gudangkita.databinding.ItemStaffBinding

class StaffAdapter(private val staffList: List<User>) :
    RecyclerView.Adapter<StaffAdapter.StaffViewHolder>() {

    inner class StaffViewHolder(val binding: ItemStaffBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val binding = ItemStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val staff = staffList[position]
        holder.binding.tvName.text = staff.name
        holder.binding.tvEmail.text = staff.email
    }

    override fun getItemCount(): Int = staffList.size
}