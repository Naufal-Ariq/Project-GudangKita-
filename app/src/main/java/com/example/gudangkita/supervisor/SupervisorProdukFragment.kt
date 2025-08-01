package com.example.gudangkita.supervisor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gudangkita.add.AddProductActivity
import com.example.gudangkita.databinding.FragmentSupervisorProdukBinding
import com.google.firebase.database.FirebaseDatabase

class SupervisorProdukFragment : Fragment() {
    private var _binding: FragmentSupervisorProdukBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SupervisorHomeViewModel by activityViewModels()
    private lateinit var productAdapter: SupervisorProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupervisorProdukBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        binding.fabAddProduct.setOnClickListener {
            val intent = Intent(requireContext(), AddProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        productAdapter = SupervisorProductAdapter(
            products = emptyList(),
            onEditClick = { product ->
                val intent = Intent(requireContext(), AddProductActivity::class.java).apply {
                    putExtra("product_id", product.id)
                    putExtra("name", product.name)
                    putExtra("sku", product.sku)
                    putExtra("quantity", product.quantity)
                    putExtra("location", product.location)
                    putExtra("isPicked", product.isPicked)
                }
                startActivity(intent)
            },
            onDeleteClick = { product ->
                FirebaseDatabase.getInstance().getReference("products")
                    .child(product.id)
                    .removeValue()
            }
        )

        binding.rvProductSupervisor.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        viewModel.productList.observe(viewLifecycleOwner) { products ->
            productAdapter.updateData(products)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}