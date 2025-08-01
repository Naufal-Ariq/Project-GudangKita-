package com.example.gudangkita.add

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gudangkita.R
import com.example.gudangkita.common.Order
import com.example.gudangkita.databinding.ActivityAddOrderBinding
import com.example.gudangkita.common.ActivityLogger
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import java.util.UUID

class AddOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddOrderBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSimpan.setOnClickListener {
            simpanPesanan()
        }
    }

    private fun simpanPesanan() {
        val orderId = UUID.randomUUID().toString()
        val tujuan = binding.etTujuan.text.toString().trim()
        val itemCountStr = binding.etJumlahBarang.text.toString().trim()
        val status = binding.spinnerStatus.selectedItem.toString()

        if (tujuan.isEmpty() || itemCountStr.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        val order = Order(
            orderId = orderId,
            tujuan = tujuan,
            status = status,
            itemCount = itemCountStr.toLongOrNull() ?: 0,
            assignedToUid = "", // Kosong, bisa diisi nanti
            createdAt = Date()
        )

        db.collection("orders")
            .document(orderId)
            .set(order)
            .addOnSuccessListener {
                // ✅ Tambahkan log aktivitas jika status = diproses
                if (status.lowercase() == "diproses") {
                    ActivityLogger.logActivity(
                        message = "Memproses pesanan #$orderId ke $tujuan",
                        iconResId = R.drawable.ic_process // Gunakan icon proses sesuai project-mu
                    )
                }

                Toast.makeText(this, "Pesanan berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }

            .addOnFailureListener {
                Toast.makeText(this, "Gagal menyimpan pesanan", Toast.LENGTH_SHORT).show()
            }
    }
}