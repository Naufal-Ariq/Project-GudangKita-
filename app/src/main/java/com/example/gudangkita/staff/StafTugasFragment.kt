package com.example.gudangkita.staff

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gudangkita.R

class StafTugasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StafOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_staf_tugas, container, false)

        recyclerView = view.findViewById(R.id.recycler_staf_orders)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sampleOrders = listOf(
            StafOrder("ORD-001", "Toko Jaya Abadi", "3 Jenis Barang", "Diproses"),
            StafOrder("ORD-002", "Gudang Utama", "5 Jenis Barang", "Selesai")
        )

        adapter = StafOrderAdapter(sampleOrders) { order ->
            val intent = Intent(requireContext(), StafTaskDetailActivity::class.java)
            intent.putExtra("ORDER_ID", order.orderId)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        return view
    }
}