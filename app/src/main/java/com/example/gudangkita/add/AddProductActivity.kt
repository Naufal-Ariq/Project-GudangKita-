package com.example.gudangkita.add

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gudangkita.R
import com.example.gudangkita.common.Product
import com.google.firebase.firestore.FirebaseFirestore

class AddProductActivity : AppCompatActivity() {

    private lateinit var etProductName: EditText
    private lateinit var etProductSku: EditText
    private lateinit var spinnerQuantity: Spinner
    private lateinit var etProductLocation: EditText
    private lateinit var btnSaveProduct: Button
    private lateinit var btnDeleteProduct: Button
    private lateinit var imgProduct: ImageView
    private lateinit var btnPickImage: Button


    private var productId: String? = null
    private var isEditMode = false

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        val toolbar: Toolbar = findViewById(R.id.toolbar_add_product)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        etProductName = findViewById(R.id.et_product_name)
        etProductSku = findViewById(R.id.et_product_sku)
        spinnerQuantity = findViewById(R.id.spinner_product_quantity)
        etProductLocation = findViewById(R.id.et_product_location)
        btnSaveProduct = findViewById(R.id.btn_save_product)
        btnDeleteProduct = findViewById(R.id.btn_delete_product)
        imgProduct = findViewById(R.id.img_product)
        btnPickImage = findViewById(R.id.btn_pick_image)


        val quantityList = (1..100).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, quantityList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerQuantity.adapter = adapter

        val product = intent.getSerializableExtra("product") as? Product
        if (product != null) {
            isEditMode = true
            productId = product.id
            etProductName.setText(product.name)
            etProductSku.setText(product.sku)
            etProductLocation.setText(product.location)
            val selectedIndex = quantityList.indexOf(product.quantity.toInt())
            spinnerQuantity.setSelection(if (selectedIndex >= 0) selectedIndex else 0)
            btnSaveProduct.text = "Simpan Perubahan"
            btnDeleteProduct.visibility = Button.VISIBLE
        }

        btnSaveProduct.setOnClickListener {
            val name = etProductName.text.toString().trim()
            val sku = etProductSku.text.toString().trim()
            val location = etProductLocation.text.toString().trim()
            val quantity = spinnerQuantity.selectedItem as Int

            if (name.isBlank() || sku.isBlank() || location.isBlank()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = productId ?: db.collection("products").document().id
            val productData = Product(id, name, sku, quantity.toLong(), location, null, false)


            db.collection("products")
                .document(id)
                .set(productData)
                .addOnSuccessListener {
                    Log.d("AddProduct", "Produk berhasil disimpan ke Firestore")
                    Toast.makeText(
                        this,
                        if (isEditMode) "Produk berhasil diperbarui" else "Produk berhasil ditambahkan",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menyimpan: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("AddProduct", "Firestore error saat menyimpan", e)
                }
        }

        btnDeleteProduct.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Produk?")
                .setMessage("Apakah Anda yakin ingin menghapus produk ini?")
                .setPositiveButton("Ya") { _, _ ->
                    val id = productId ?: return@setPositiveButton
                    db.collection("products")
                        .document(id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Produk dihapus", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal menghapus produk", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            showExitConfirmationDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Keluar tanpa menyimpan?")
            .setMessage("Perubahan belum disimpan. Apakah Anda yakin ingin kembali?")
            .setPositiveButton("Ya") { _, _ -> finish() }
            .setNegativeButton("Batal", null)
            .show()
    }
}