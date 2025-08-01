package com.example.gudangkita.common

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

// Data class untuk pesanan
data class Order(
    val orderId: String = "",          // ID pesanan
    val tujuan: String = "",           // Tujuan pengiriman
    val status: String = "",           // Status pesanan
    val itemCount: Long = 0,           // Jumlah jenis barang
    val assignedToUid: String = "",    // UID staf yang ditugaskan
    @ServerTimestamp
    val createdAt: Date? = null        // Timestamp saat dibuat (diisi otomatis oleh Firestore)
)