package com.example.gudangkita.supervisor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gudangkita.add.AddOrderActivity
import com.example.gudangkita.common.Order
import com.example.gudangkita.common.OrderAdapter
import com.example.gudangkita.databinding.FragmentSupervisorOrderBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class SupervisorOrderFragment : Fragment() {

    private var _binding: FragmentSupervisorOrderBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private lateinit var orderAdapter: OrderAdapter
    private val orders = mutableListOf<Order>()
    private var orderListener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupervisorOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        listenToOrders()

        // 🔹 Integrasi tombol tambah pesanan
        binding.fabAddOrder.setOnClickListener {
            val intent = Intent(requireContext(), AddOrderActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(
            orders,
            onProsesClicked = { order -> updateOrderStatus(order.orderId, "Diproses") },
            onSelesaiClicked = { order -> updateOrderStatus(order.orderId, "Selesai") }
        )

        binding.rvSupervisorOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun listenToOrders() {
        orderListener = db.collection("orders")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null || snapshots == null) return@addSnapshotListener

                orders.clear()
                for (doc in snapshots) {
                    val order = doc.toObject(Order::class.java)
                    orders.add(order)
                }
                orderAdapter.notifyDataSetChanged()

                binding.tvEmptyState.visibility = if (orders.isEmpty()) View.VISIBLE else View.GONE
            }
    }

    private fun updateOrderStatus(orderId: String, newStatus: String) {
        db.collection("orders").document(orderId)
            .update("status", newStatus)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        orderListener?.remove()
        _binding = null
    }
}