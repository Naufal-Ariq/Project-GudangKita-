package com.example.gudangkita.staff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gudangkita.common.Order
import com.example.gudangkita.common.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StafHomeViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    init {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            fetchOrdersFor(currentUser.uid)
            fetchUserName(currentUser.uid)
        }
    }

    private fun fetchOrdersFor(userId: String) {
        _isLoading.value = true
        db.collection("orders")
            .whereEqualTo("assignedToUid", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                _isLoading.value = false
                if (error != null) { return@addSnapshotListener }

                snapshot?.let {
                    _orders.value = it.toObjects(Order::class.java)
                }
            }
    }

    private fun fetchUserName(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    _userName.value = user?.name ?: "Staf Gudang"
                }
            }
            .addOnFailureListener {
                _userName.value = "Staf Gudang"
            }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        FirebaseFirestore.getInstance()
            .collection("orders")
            .document(orderId)
            .update("status", newStatus)
    }

}