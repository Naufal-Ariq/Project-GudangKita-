package com.example.gudangkita.common

import java.io.Serializable

data class Product(
    val id: String = "",
    val name: String = "",
    val sku: String = "",
    val quantity: Long = 0,
    val location: String = "",
    var imageUrl: String? = null,
    var isPicked: Boolean = false
) : Serializable