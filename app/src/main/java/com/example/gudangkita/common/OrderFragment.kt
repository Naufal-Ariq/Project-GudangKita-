package com.example.gudangkita.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gudangkita.databinding.FragmentOrderBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    private lateinit var orderAdapter: OrderAdapter
    private var orderListener: ListenerRegistration? = null
    private val orders = mutableListOf<Order>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        listenToOrders()
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(
            orders,
            onProsesClicked = { order -> updateOrderStatus(order, "Diproses") },
            onSelesaiClicked = { order -> updateOrderStatus(order, "Selesai") }
        )

        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }


    private fun listenToOrders() {
        orderListener = db.collection("orders")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) return@addSnapshotListener

                orders.clear()
                for (doc in snapshots!!) {
                    val order = doc.toObject(Order::class.java)
                    orders.add(order)
                }
                orderAdapter.notifyDataSetChanged()
            }
    }

    private fun updateOrderStatus(order: Order, newStatus: String) {
        db.collection("orders")
            .document(order.orderId)
            .update("status", newStatus)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        orderListener?.remove()
        _binding = null
    }
}