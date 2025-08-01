package com.example.gudangkita.common

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

object ActivityLogger {
    fun logActivity(message: String, iconResId: Int) {
        val log = ActivityLog(
            message = message,
            iconResId = iconResId,
            timestamp = Timestamp.Companion.now() // ✅ Pakai Timestamp, bukan Long
        )

        FirebaseFirestore.getInstance()
            .collection("activity_logs")
            .add(log)
            .addOnSuccessListener {
                // Optional: log berhasil disimpan
                println("Log aktivitas berhasil disimpan.")
            }
            .addOnFailureListener { e ->
                // Optional: log gagal disimpan
                e.printStackTrace()
            }
    }
}