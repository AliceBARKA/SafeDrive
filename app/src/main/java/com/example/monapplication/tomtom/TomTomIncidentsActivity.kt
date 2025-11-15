package com.example.monapplication.tomtom

import android.Manifest
import android.R.attr.type
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.monapplication.R
import com.example.monapplication.ui.theme.alertes.EditDangerActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.math.pow

class TomTomIncidentsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TomTomAdapter
    private val incidentsList = mutableListOf<TomTomIncident>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var db: FirebaseFirestore

    private lateinit var radiusSpinner: Spinner
    private var selectedRadiusKm = 5.0

    private val tomtomApiKey = "KrmYX8v89W7QRGMTwNRVj3mssTz6Zy0R"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tomtom_incidents)

        recyclerView = findViewById(R.id.incidentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TomTomAdapter(
            incidents = incidentsList,
            onDeleteClicked = { docId -> deleteIncidentFromFirestore(docId) },
            onEditClicked = { incident -> openEditActivity(incident.documentId) }
        )
        recyclerView.adapter = adapter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        db = FirebaseFirestore.getInstance()

        radiusSpinner = findViewById(R.id.radiusSpinner)
        val radiusOptions = listOf("5 km", "10 km", "15 km")
        radiusSpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, radiusOptions)

        radiusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedRadiusKm = (position + 1) * 5.0
                getLocationAndLoadIncidents()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        } else {
            getLocationAndLoadIncidents()
        }
    }

    override fun onResume() {
        super.onResume()
        getLocationAndLoadIncidents()
    }

    private fun getLocationAndLoadIncidents() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permission GPS non accordée", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                loadIncidentsFromTomTom(location)
            } else {
                Log.e("TomTomAPI", "Position introuvable.")
            }
        }
    }

    private fun deleteIncidentFromFirestore(documentId: String) {
        db.collection("dangers").document(documentId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Danger supprimé ✅", Toast.LENGTH_SHORT).show()

                // ❗ Ne recharge pas tout de suite, nettoie juste localement
                incidentsList.removeAll { it.documentId == documentId }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erreur suppression : ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun openEditActivity(documentId: String?) {
        if (documentId == null) return
        val intent = Intent(this, EditDangerActivity::class.java)
        intent.putExtra("documentId", documentId)
        startActivity(intent)
    }

    private fun loadIncidentsFromTomTom(location: Location) {
        val lat = location.latitude
        val lon = location.longitude
        val delta = selectedRadiusKm / 111.0
        val bbox = "${lon - delta},${lat - delta},${lon + delta},${lat + delta}"
        val url = "https://api.tomtom.com/traffic/services/5/incidentDetails?key=$tomtomApiKey&bbox=$bbox"

        Log.d("TomTomAPI", "URL utilisée : $url")

        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TomTomAPI", "Erreur de requête : ${e.message}")
                runOnUiThread {
                    loadFirestoreIncidents(location)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val tempList = mutableListOf<TomTomIncident>()
                try {
                    val json = JSONObject(body)
                    val incidents = json.getJSONArray("incidents")
                    val total = incidents.length()
                    var completed = 0

                    if (total == 0) {
                        runOnUiThread { loadFirestoreIncidents(location) }
                        return
                    }

                    for (i in 0 until total) {
                        val incident = incidents.getJSONObject(i)
                        val props = incident.getJSONObject("properties")
                        val geometry = incident.getJSONObject("geometry")
                        val coordinatesArray = geometry.getJSONArray("coordinates")
                        val firstPoint = if (coordinatesArray.get(0) is org.json.JSONArray)
                            coordinatesArray.getJSONArray(0)
                        else coordinatesArray

                        val lon = firstPoint.getDouble(0)
                        val lat = firstPoint.getDouble(1)

                        val iconCat = props.optInt("iconCategory", -1)
                        val category = getCategoryName(iconCat)
                        val description = props.optString("eventDescription", "").ifBlank {
                            "🚧 $category signalé"
                        }

                        fetchAddressFromCoordinates(lat, lon, location, category) { address ->
                            val item = TomTomIncident(category, "$description\n$address", lat, lon)
                            tempList.add(item)
                            completed++

                            if (completed == total) {
                                runOnUiThread {
                                    incidentsList.clear()
                                    incidentsList.addAll(tempList)
                                    loadFirestoreIncidents(location)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("TomTomAPI", "Erreur parsing JSON : ${e.message}")
                    runOnUiThread {
                        loadFirestoreIncidents(location)
                    }
                }
            }
        })
    }



    private fun loadFirestoreIncidents(location: Location) {
        val lat = location.latitude
        val lon = location.longitude
        val radius = selectedRadiusKm

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        // 🔥 Supprime les incidents Firestore déjà présents
        incidentsList.removeAll { it.documentId != null }

        db.collection("dangers").get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val type = doc.getString("type") ?: "Inconnu"
                    val details = doc.getString("details") ?: ""
                    val locationText = doc.getString("location") ?: "Lieu non précisé"
                    val latitude = doc.getDouble("latitude") ?: continue
                    val longitude = doc.getDouble("longitude") ?: continue

                    val distance = distanceBetween(lat, lon, latitude, longitude)
                    if (distance <= radius) {
                        val incident = TomTomIncident(
                            category = "⚠️ $type",
                            description = "$details\n📍 $locationText",
                            latitude = latitude,
                            longitude = longitude,
                            documentId = doc.id
                        )
                        incidentsList.add(incident)
                    }
                }

                // ✅ Correction ici : séparer les incidents TomTom et Firestore pour éviter de supprimer les TomTom
                val firestoreIncidents = incidentsList.filter { it.documentId != null }
                val uniqueFirestore = firestoreIncidents.distinctBy { it.documentId }

                val tomtomIncidents = incidentsList.filter { it.documentId == null }

                incidentsList.clear()
                incidentsList.addAll(tomtomIncidents + uniqueFirestore)

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.e("Firestore", "Erreur récupération alertes manuelles : ${it.message}")
            }
    }


    private fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2).pow(2.0) +
                Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2).pow(2.0)
        return 2 * earthRadius * Math.asin(Math.sqrt(a))
    }

    private fun getCategoryName(iconCategory: Int): String {
        return when (iconCategory) {
            1 -> "Accident"
            2 -> "Route fermée"
            3 -> "Travaux"
            4 -> "Événement public"
            5 -> "Danger"
            6 -> "Météo"
            7 -> "Obstruction"
            8 -> "Bouchon"
            9 -> "Circulation dense"
            else -> "Autre"
        }
    }

    private fun fetchAddressFromCoordinates(
        lat: Double,
        lon: Double,
        userLocation: Location,
        category: String,
        callback: (String) -> Unit
    ) {
        val url = "https://api.tomtom.com/search/2/reverseGeocode/$lat,$lon.json?key=$tomtomApiKey"
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val distance =
                    distanceBetween(userLocation.latitude, userLocation.longitude, lat, lon)
                val fallback = "📍 $category à ~${"%.1f".format(distance)} km"
                callback(fallback)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                try {
                    val json = JSONObject(body)
                    val address = json.optJSONArray("addresses")
                        ?.optJSONObject(0)
                        ?.optJSONObject("address")
                        ?.optString("freeformAddress")

                    val distance =
                        distanceBetween(userLocation.latitude, userLocation.longitude, lat, lon)

                    val finalAddress = if (!address.isNullOrBlank()) {
                        "$address\n(à ${"%.1f".format(distance)} km)"
                    } else {
                        "📍 $category à ~${"%.1f".format(distance)} km"
                    }

                    callback(finalAddress)

                } catch (e: Exception) {
                    val distance =
                        distanceBetween(userLocation.latitude, userLocation.longitude, lat, lon)
                    val fallback = "📍 $category à ~${"%.1f".format(distance)} km"
                    callback(fallback)
                }
            }
        })
    }
}


