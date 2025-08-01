package com.example.gudangkita.common

data class User(
    val name: String = "",             // Nama user
    val email: String = "",            // Email user
    val role: String = ""              // Peran (admin, staf, dll)
)