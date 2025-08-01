package com.example.gudangkita.staff

data class TaskItem(
    val name: String,       // Nama barang / pesanan
    val location: String,   // Lokasi rak barang
    val isScanned: Boolean  // Status sudah discan atau belum
)
