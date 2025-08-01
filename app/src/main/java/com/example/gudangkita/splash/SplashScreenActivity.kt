package com.example.gudangkita.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.gudangkita.R
import com.example.gudangkita.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var logo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        logo = findViewById(R.id.logo_splash)

        // Jalankan animasi fade + scale
        val anim = AnimationUtils.loadAnimation(this, R.anim.fade_scale)
        logo.startAnimation(anim)

        // Delay ke LoginActivity (2 detik)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }
}