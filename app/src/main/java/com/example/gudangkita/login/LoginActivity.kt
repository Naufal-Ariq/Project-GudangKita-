package com.example.gudangkita.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gudangkita.databinding.ActivityLoginBinding
import com.example.gudangkita.staff.StafHomeActivity
import com.example.gudangkita.supervisor.SupervisorHomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private val TAG = "LOGIN_DEBUG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inisialisasi View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Inisialisasi Firebase
        auth = Firebase.auth
        db = Firebase.firestore

        // 3. Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("893714144576-v84a53f7k546dkqvos9bvaajvdjqlfvd.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 4. Event Listener untuk tombol-tombol
        binding.btnGoogleLogin.setOnClickListener {
            signInWithGoogle()
        }

        binding.btnLoginStaf.setOnClickListener {
            Log.d(TAG, "Tombol Login Staf ditekan.")
            performLogin()
        }

        binding.btnLoginSupervisor.setOnClickListener {
            Log.d(TAG, "Tombol Login Supervisor ditekan.")
            performLogin()
        }

        Log.d(TAG, "LoginActivity berhasil dimuat.")
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        Log.d(TAG, "Mencoba login dengan email: $email")

        if (validateInput(email, password)) {
            loginUser(email, password)
        }
    }

    private fun validateInput(email: String, pass: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email tidak boleh kosong"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Format email tidak valid"
            return false
        }
        if (pass.isEmpty()) {
            binding.etPassword.error = "Password tidak boleh kosong"
            return false
        }
        return true
    }

    private fun loginUser(email: String, pass: String) {
        showLoading(true)
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        Log.d(TAG, "Login ke Firebase Authentication BERHASIL. UID: ${user.uid}")
                        checkUserRole(user.uid)
                    } else {
                        showLoading(false)
                        Log.e(TAG, "Login berhasil tapi user null.")
                        Toast.makeText(this, "Gagal mendapatkan data pengguna setelah login.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    showLoading(false)
                    Log.e(TAG, "Login ke Firebase Authentication GAGAL. Error: ${task.exception?.message}")
                    Toast.makeText(this, "Login Gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkUserRole(uid: String) {
        showLoading(true)
        Log.d(TAG, "Mengecek role untuk UID: $uid di collection 'users'")
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val role = document.getString("role")
                    Log.d(TAG, "Dokumen ditemukan. Role dari database: $role")
                    when (role) {
                        "staf" -> {
                            Log.d(TAG, "Role adalah 'staf'. Navigasi ke StafHomeActivity.")
                            val intent = Intent(this, StafHomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        "supervisor" -> {
                            Log.d(TAG, "Role adalah 'supervisor'. Navigasi ke SupervisorHomeActivity.")
                            val intent = Intent(this, SupervisorHomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        else -> {
                            showLoading(false)
                            Log.e(TAG, "Role tidak dikenali atau null. Role: $role")
                            Toast.makeText(this, "Peran pengguna tidak dikenali.", Toast.LENGTH_LONG).show()
                            auth.signOut()
                        }
                    }
                } else {
                    showLoading(false)
                    Log.e(TAG, "Dokumen untuk UID $uid TIDAK DITEMUKAN di Firestore.")
                    Toast.makeText(this, "Data pengguna tidak ditemukan di database.", Toast.LENGTH_LONG).show()
                    auth.signOut()
                }
            }
            .addOnFailureListener { exception ->
                showLoading(false)
                Log.e(TAG, "Gagal mengambil dokumen dari Firestore. Error: ${exception.message}")
                Toast.makeText(this, "Gagal mengambil data peran: ${exception.message}", Toast.LENGTH_LONG).show()
                auth.signOut()
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Login dengan Google gagal.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        checkUserRole(user.uid)
                    }
                } else {
                    Toast.makeText(this, "Autentikasi gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLoginStaf.isEnabled = !isLoading
        binding.btnLoginSupervisor.isEnabled = !isLoading
    }
}