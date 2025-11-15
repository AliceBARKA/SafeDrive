package com.example.monapplication.ui.theme.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.monapplication.ui.theme.home.HomeActivity
import com.example.monapplication.R
import com.google.firebase.auth.FirebaseAuth
import android.util.Patterns

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialisation de FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // 🔹 Vérifier si l'utilisateur est déjà connecté
        if (auth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish() // 🔹 Fermer LoginActivity
        }

        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            // 🔹 Vérification des champs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔹 Vérification du format de l'email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Veuillez entrer un email valide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔹 Connexion avec Firebase Auth
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Connexion réussie
                        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish() // Fermer LoginActivity
                    } else {
                        // Erreur lors de la connexion
                        Toast.makeText(this, "Erreur : ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
