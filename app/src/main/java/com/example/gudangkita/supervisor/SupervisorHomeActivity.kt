package com.example.gudangkita.supervisor

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gudangkita.login.LoginActivity
import com.example.gudangkita.R
import com.example.gudangkita.supervisor.SupervisorHomeViewModel
import com.example.gudangkita.databinding.ActivitySupervisorHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SupervisorHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupervisorHomeBinding
    private val viewModel: SupervisorHomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupervisorHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_supervisor) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationSupervisor.setupWithNavController(navController)

        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {
        viewModel.userName.observe(this) { name ->
            binding.tvSupervisorName.text = "$name (Supervisor)"
        }
    }

    private fun setupListeners() {
        binding.btnLogoutSupervisor.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

}