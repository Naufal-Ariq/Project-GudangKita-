package com.example.gudangkita.supervisor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gudangkita.common.User
import com.example.gudangkita.databinding.FragmentSupervisorStafBinding
import com.example.gudangkita.staff.StaffAdapter
import com.google.firebase.firestore.FirebaseFirestore

class SupervisorStafFragment : Fragment() {

    private var _binding: FragmentSupervisorStafBinding? = null
    private val binding get() = _binding!!

    private val staffList = mutableListOf<User>()
    private lateinit var adapter: StaffAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupervisorStafBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = StaffAdapter(staffList)
        binding.rvStaff.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStaff.adapter = adapter

        fetchStaffFromFirestore()
    }

    private fun fetchStaffFromFirestore() {
        db.collection("users")
            .whereEqualTo("role", "staf")
            .get()
            .addOnSuccessListener { result ->
                staffList.clear() // Kosongkan dulu list lama
                for (document in result) {
                    val user = document.toObject(User::class.java)
                    staffList.add(user)
                }
                adapter.notifyDataSetChanged() // Beri tahu adapter bahwa data berubah
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Gagal mengambil staf: ", exception)
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}