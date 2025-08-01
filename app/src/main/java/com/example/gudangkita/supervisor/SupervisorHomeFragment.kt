package com.example.gudangkita.supervisor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gudangkita.common.ActivityLogAdapter
import com.example.gudangkita.supervisor.SupervisorHomeViewModel
import com.example.gudangkita.databinding.FragmentSupervisorHomeBinding

class SupervisorHomeFragment : Fragment() {

    private var _binding: FragmentSupervisorHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SupervisorHomeViewModel by activityViewModels()
    private lateinit var logAdapter: ActivityLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupervisorHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        logAdapter = ActivityLogAdapter(emptyList())
        binding.rvActivityLogs.apply {
            adapter = logAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        // Statistik produk
        viewModel.totalProduk.observe(viewLifecycleOwner) {
            binding.tvTotalProduk.text = it.toString()
        }

        viewModel.stokHampirHabis.observe(viewLifecycleOwner) {
            binding.tvStokHabis.text = it.toString()
        }

        // Statistik pesanan
        viewModel.pesananDiproses.observe(viewLifecycleOwner) {
            binding.tvPesananDiproses.text = it.toString()
        }

        viewModel.pesananSelesai.observe(viewLifecycleOwner) {
            binding.tvPesananSelesai.text = it.toString()
        }

        // Aktivitas log terbaru
        viewModel.activityLogs.observe(viewLifecycleOwner) { logs ->
            logAdapter.updateLogs(logs)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}