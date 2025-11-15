package com.example.monapplication.ui.theme.home
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.monapplication.R
import com.example.monapplication.tomtom.TomTomIncidentsActivity
import com.example.monapplication.ui.service.BrakeDetectionService
import com.example.monapplication.ui.theme.alertes.ReportDangerActivity
import com.example.monapplication.ui.theme.auth.EditProfileActivity
import com.example.monapplication.ui.theme.auth.LoginActivity
import com.example.monapplication.ui.theme.splash.SplashActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import okhttp3.*
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val userName = findViewById<TextView>(R.id.userName)
        val userAddress = findViewById<TextView>(R.id.userAddress)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val prenom = document.getString("prenom") ?: "Prénom inconnu"
                    val nom = document.getString("nom") ?: "Nom inconnu"

                    val fullName = "$prenom $nom"
                    val address = document.getString("email") ?: "Email inconnu"

                    findViewById<TextView>(R.id.userName).text = fullName
                    findViewById<TextView>(R.id.userAddress).text = address
                }
                .addOnFailureListener {
                    findViewById<TextView>(R.id.userName).text = "Erreur"
                    findViewById<TextView>(R.id.userAddress).text = "Erreur"
                }
        }


        val reportDangerButton = findViewById<Button>(R.id.reportDangerButton)

        val logoutButton = findViewById<Button>(R.id.logoutButton)
        val startButton = findViewById<Button>(R.id.startButton)
        val tomTomButton = findViewById<Button>(R.id.tomTomButton)
        val editProfileButton = findViewById<Button>(R.id.editProfileButton)

        startButton.setOnClickListener {
            val intent = Intent(this, DetectionActivity::class.java)
            startActivity(intent)
        }


        logoutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


        reportDangerButton.setOnClickListener {
            val intent = Intent(this, ReportDangerActivity::class.java)
            startActivity(intent)
        }


        tomTomButton.setOnClickListener {
            startActivity(Intent(this, TomTomIncidentsActivity::class.java))
        }

        editProfileButton.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }


        }



    }




