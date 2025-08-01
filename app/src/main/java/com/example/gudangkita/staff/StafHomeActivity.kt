package com.example.gudangkita.staff

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gudangkita.R
import com.example.gudangkita.databinding.ActivityStafHomeBinding
import com.example.gudangkita.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StafHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStafHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStafHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil NavController dari FragmentContainerView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_staf) as NavHostFragment
        val navController = navHostFragment.navController

        // Hubungkan BottomNavigationView dengan NavController
        binding.bottomNavigationStaf.setupWithNavController(navController)

        // Set data dummy untuk header (nanti bisa dari Firebase)
        binding.tvStafName.text = "Andi (Staf Gudang)"

        // Listener tombol Logout
        binding.btnLogoutStaf.setOnClickListener {
            Firebase.auth.signOut() // keluar dari akun
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}
