package com.example.gudangkita.staff

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gudangkita.common.OrderItemAdapter
import com.example.gudangkita.R
import com.example.gudangkita.common.ScannerActivity
import com.google.android.material.button.MaterialButton

class StafTaskDetailActivity : AppCompatActivity() {

    private lateinit var rvItems: RecyclerView
    private lateinit var adapter: OrderItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staf_task_detail)

        val orderId = intent.getStringExtra("ORDER_ID") ?: "Unknown"

        rvItems = findViewById(R.id.rv_order_items)
        rvItems.layoutManager = LinearLayoutManager(this)

        // Dummy data barang
        val items = listOf(
            OrderItem("Buku Tulis", 10, "Rak A-01"),
            OrderItem("Pulpen Hitam", 20, "Rak B-03"),
            OrderItem("Kertas A4", 5, "Rak A-02")
        )

        adapter = OrderItemAdapter(items)
        rvItems.adapter = adapter

        val btnScan = findViewById<MaterialButton>(R.id.btn_scan_next_item)
        btnScan.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }
    }
}

data class OrderItem(val name: String, val qty: Int, val location: String)
