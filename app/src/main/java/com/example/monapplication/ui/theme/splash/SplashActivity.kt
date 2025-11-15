package com.example.monapplication.ui.theme.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.monapplication.R
import com.example.monapplication.ui.theme.auth.LoginActivity
import com.example.monapplication.ui.theme.auth.SignupActivity
import com.example.monapplication.ui.theme.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // ✅ Vérifie si l'utilisateur est déjà connecté
        if (auth.currentUser != null) {
            // 🔁 Redirige vers HomeActivity directement
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            // 👇 Sinon, affiche la page splash avec les boutons
            setContentView(R.layout.activity_splash)

            findViewById<Button>(R.id.loginButton).setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }

            findViewById<Button>(R.id.signupButton).setOnClickListener {
                startActivity(Intent(this, SignupActivity::class.java))
            }
        }
    }
}
