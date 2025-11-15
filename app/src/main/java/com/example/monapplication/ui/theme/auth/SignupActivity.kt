package com.example.monapplication.ui.theme.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.monapplication.R
import com.example.monapplication.ui.theme.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nomField = findViewById<EditText>(R.id.nom)
        val prenomField = findViewById<EditText>(R.id.prenom)
        val contact1Field = findViewById<EditText>(R.id.contact1)
        val contact2Field = findViewById<EditText>(R.id.contact2)
        val emailField = findViewById<EditText>(R.id.email)
        val passwordField = findViewById<EditText>(R.id.password)
        val signupButton = findViewById<Button>(R.id.signupButton)

        signupButton.setOnClickListener {
            Log.d("DEBUG_SIGNUP", "Bouton S'inscrire cliqué")

            val nom = nomField.text.toString().trim()
            val prenom = prenomField.text.toString().trim()
            val contact1 = contact1Field.text.toString().trim()
            val contact2 = contact2Field.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (nom.isEmpty() || prenom.isEmpty() || contact1.isEmpty() || contact2.isEmpty() || email.isEmpty() || password.length < 6) {
                Log.d("DEBUG_SIGNUP", "Champs invalides")
                Toast.makeText(this, "Veuillez remplir tous les champs correctement", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("DEBUG_SIGNUP", "Tous les champs sont valides")

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("DEBUG_SIGNUP", "Création de compte réussie")

                        val userId = auth.currentUser!!.uid
                        val user = hashMapOf(
                            "nom" to nom,
                            "prenom" to prenom,
                            "contact1" to contact1,
                            "contact2" to contact2,
                            "email" to email
                        )

                        db.collection("users").document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d("DEBUG_SIGNUP", "Données utilisateur enregistrées")
                                Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show()

                                Log.d("DEBUG_SIGNUP", "Redirection vers Home")
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.e("DEBUG_SIGNUP", "Erreur Firestore : ${e.message}")
                                Toast.makeText(this, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                            }

                    } else {
                        Log.e("DEBUG_SIGNUP", "Erreur création utilisateur : ${task.exception?.message}")
                        Toast.makeText(this, "Erreur : ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
