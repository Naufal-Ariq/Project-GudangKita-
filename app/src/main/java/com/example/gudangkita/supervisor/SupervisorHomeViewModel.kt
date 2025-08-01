package com.example.gudangkita.supervisor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gudangkita.common.ActivityLog
import com.example.gudangkita.common.Product
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SupervisorHomeViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    // LiveData untuk dashboard
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _totalProduk = MutableLiveData(0)
    val totalProduk: LiveData<Int> = _totalProduk

    private val _stokHampirHabis = MutableLiveData(0)
    val stokHampirHabis: LiveData<Int> = _stokHampirHabis

    private val _pesananDiproses = MutableLiveData(0)
    val pesananDiproses: LiveData<Int> = _pesananDiproses

    private val _pesananSelesai = MutableLiveData(0)
    val pesananSelesai: LiveData<Int> = _pesananSelesai

    private val _activityLogs = MutableLiveData<List<ActivityLog>>()
    val activityLogs: LiveData<List<ActivityLog>> = _activityLogs

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> = _productList

    init {
        auth.currentUser?.uid?.let { uid ->
            fetchUserName(uid)
            observeDashboardData()
            observeActivityLogs()
            observeProductList()
        }
    }

    /** Mendapatkan nama pengguna berdasarkan UID */
    private fun fetchUserName(uid: String) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                _userName.value = doc.getString("name") ?: "Supervisor"
            }
            .addOnFailureListener {
                Log.e("SupervisorVM", "Gagal mengambil nama pengguna", it)
            }
    }

    /** Mengamati data real-time untuk dashboard supervisor */
    private fun observeDashboardData() {
        // Total Produk
        db.collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("SupervisorVM", "Gagal ambil total produk", error)
                    return@addSnapshotListener
                }
                _totalProduk.value = snapshot?.size() ?: 0
            }

        // Stok Hampir Habis
        db.collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("SupervisorVM", "Gagal ambil stok hampir habis", error)
                    return@addSnapshotListener
                }
                val lowStock = snapshot?.documents?.count {
                    (it.getLong("quantity") ?: 0) < 10
                } ?: 0
                _stokHampirHabis.value = lowStock
            }

        // Pesanan Diproses
        db.collection("orders")
            .whereEqualTo("status", "Diproses")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("SupervisorVM", "Gagal ambil pesanan diproses", error)
                    return@addSnapshotListener
                }
                _pesananDiproses.value = snapshot?.size() ?: 0
            }

        // Pesanan Selesai
        db.collection("orders")
            .whereEqualTo("status", "Selesai")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("SupervisorVM", "Gagal ambil pesanan selesai", error)
                    return@addSnapshotListener
                }
                _pesananSelesai.value = snapshot?.size() ?: 0
            }
    }

    /** Mengamati log aktivitas terbaru */
    private fun observeActivityLogs() {
        db.collection("activity_logs")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(5)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("SupervisorVM", "Gagal ambil activity logs", error)
                    return@addSnapshotListener
                }
                _activityLogs.value = snapshot?.toObjects(ActivityLog::class.java) ?: emptyList()
            }
    }

    /** Menambahkan log aktivitas baru */
    fun logActivity(message: String, iconResId: Int) {
        val log = hashMapOf(
            "message" to message,
            "iconResId" to iconResId,
            "timestamp" to Timestamp.Companion.now()
        )
        db.collection("activity_logs")
            .add(log)
            .addOnSuccessListener {
                Log.d("SupervisorVM", "Log aktivitas berhasil ditambahkan")
            }
            .addOnFailureListener { e ->
                Log.e("SupervisorVM", "Gagal menambahkan log aktivitas", e)
            }
    }

    /** Mengamati daftar produk */
    private fun observeProductList() {
        db.collection("products")
            .orderBy("name")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("SupervisorVM", "Gagal ambil daftar produk", error)
                    return@addSnapshotListener
                }
                _productList.value = snapshot?.toObjects(Product::class.java) ?: emptyList()
            }
    }
}