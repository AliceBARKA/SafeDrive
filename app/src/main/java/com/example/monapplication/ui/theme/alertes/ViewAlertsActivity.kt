/*package com.example.monapplication.ui.theme.alertes

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.monapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore

class ViewAlertsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlertAdapter
    private val alertsList = mutableListOf<DangerAlert>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_alerts)

        recyclerView = findViewById(R.id.alertsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AlertAdapter(alertsList)
        recyclerView.adapter = adapter

        // Initialiser le client de localisation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude
                    fetchAlertsInRealTime()
                } else {
                    Log.e("GPS", "Impossible d’obtenir la position actuelle")
                }
            }
        }
    }

    private fun fetchAlertsInRealTime() {
        val db = FirebaseFirestore.getInstance()
        db.collection("dangers")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("REALTIME_ALERTS", "Erreur d'écoute", e)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    alertsList.clear()
                    for (document in snapshots.documents) {
                        val type = document.getString("type") ?: "Inconnu"
                        val locationText = document.getString("location") ?: "Non spécifié"
                        val details = document.getString("details") ?: ""
                        val timestamp = document.getTimestamp("timestamp")?.toDate()

                        val latitude = document.getDouble("latitude")
                        val longitude = document.getDouble("longitude")

                        if (latitude != null && longitude != null && currentLatitude != null && currentLongitude != null) {
                            val distance = calculateDistance(
                                currentLatitude!!, currentLongitude!!,
                                latitude, longitude
                            )

                            if (distance <= 5.0) { // Filtre : 5 km
                                alertsList.add(DangerAlert(type, locationText, details, timestamp))
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }

    // Calcule la distance entre 2 points GPS (en km)
    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] / 1000.0 // Convertir en km
    }
} */