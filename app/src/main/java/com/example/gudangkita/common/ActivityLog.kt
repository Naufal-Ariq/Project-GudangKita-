package com.example.gudangkita.common

import com.google.firebase.Timestamp

data class ActivityLog(
    val message: String = "",
    val iconResId: Int = 0,
    val timestamp: Timestamp = Timestamp.Companion.now()
)