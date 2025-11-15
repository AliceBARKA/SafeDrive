package com.example.monapplication.ui.theme.alertes

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.monapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ReportDangerActivity : AppCompatActivity() {

    private lateinit var dangerTypeSpinner: Spinner
    private lateinit var locationEditText: EditText
    private lateinit var detailsEditText: EditText
    private lateinit var sendButton: Button
    private val db = FirebaseFirestore.getInstance()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    private var isSending = false // ← ajoute-la ici !

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUG_DANGER", "ReportDangerActivity créée")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_danger)
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }

        // Initialisation des vues
        dangerTypeSpinner = findViewById(R.id.dangerTypeSpinner)
        locationEditText = findViewById(R.id.locationEditText)
        detailsEditText = findViewById(R.id.detailsEditText)
        sendButton = findViewById(R.id.sendButton)

        // Liste des types de danger
        val dangerTypes = listOf("Accident", "Route barrée", "Animal", "Obstacle", "Autre")
        dangerTypeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dangerTypes)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                    Log.d("GPS", "Position récupérée : $currentLatitude, $currentLongitude")
                } else {
                    Log.e("GPS", "Position GPS introuvable")
                }
            }
        }

        // Action au clic sur "Envoyer"
        sendButton.setOnClickListener {
            if (isSending) return@setOnClickListener
            isSending = true
            sendButton.isEnabled = false

            sendReport()
        }
    }

    private fun sendReport() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val selectedType = dangerTypeSpinner.selectedItem.toString()
        val location = locationEditText.text.toString()
        val details = detailsEditText.text.toString()

        if (location.isBlank() || details.isBlank()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            isSending = false
            sendButton.isEnabled = true
            return
        }

        val report = hashMapOf(
            "type" to selectedType,
            "location" to location,
            "details" to details,
            "userId" to userId,
            "timestamp" to Date(),
            "latitude" to currentLatitude,
            "longitude" to currentLongitude
        )

        FirebaseFirestore.getInstance().collection("dangers")
            .whereEqualTo("userId", userId)
            .whereEqualTo("type", selectedType)
            .whereEqualTo("details", details)
            .whereEqualTo("location", location)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    FirebaseFirestore.getInstance().collection("dangers").add(report)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Danger signalé !", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                            isSending = false
                            sendButton.isEnabled = true
                        }
                } else {
                    Toast.makeText(this, "Ce danger a déjà été signalé !", Toast.LENGTH_SHORT).show()
                    isSending = false
                    sendButton.isEnabled = true
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erreur Firestore : ${e.message}", Toast.LENGTH_SHORT).show()
                isSending = false
                sendButton.isEnabled = true
            }
    }
}
