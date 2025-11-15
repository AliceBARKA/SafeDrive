package com.example.monapplication.ui.service


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.content.pm.PackageManager
import android.hardware.*
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.monapplication.alertes.AlertActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.sqrt

class BrakeDetectionService : Service(), SensorEventListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sensorManager: SensorManager
    private var lastAcceleration = 0f
    private var lastUpdateTime: Long = 0
    private var isAlertDisplayed = false
    private var countDownTimer: CountDownTimer? = null

    private val alertDismissReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            isAlertDisplayed = false
            countDownTimer?.cancel() // ✅ annule le timer pour empêcher le SMS
            Log.d("DEBUG_ALERT", "Alerte fermée, détection réactivée")
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }

        registerReceiver(alertDismissReceiver, IntentFilter("ALERT_DISMISSED"))



    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DEBUG_SERVICE", "Service démarré depuis bouton Start")
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - lastUpdateTime

            if (timeDifference > 100) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val acceleration = sqrt(x * x + y * y + z * z)


                if (acceleration < lastAcceleration - 5) {
                    detectBrake()
                }

                lastAcceleration = acceleration
                lastUpdateTime = currentTime

            }


        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun detectBrake() {
        showAlert()
        Log.d("SERVICE_DEBUG", "Freinage détecté !")

    }

    private fun showAlert() {
        if (isAlertDisplayed) return
        isAlertDisplayed = true

        val intent = Intent(this, AlertActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

        startTimeoutForSms()
    }

    private fun startTimeoutForSms() {
        countDownTimer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId == null) {
                    Log.e("INTENT_SMS", "Utilisateur non connecté")
                    return
                }

                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    val latitude = location?.latitude
                    val longitude = location?.longitude

                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(userId).get()
                        .addOnSuccessListener { doc ->
                            val phoneNumber1 = doc.getString("contact1")
                            val phoneNumber2 = doc.getString("contact2")

                            if (!phoneNumber1.isNullOrEmpty() && !phoneNumber2.isNullOrEmpty()) {
                                val locationPart = if (latitude != null && longitude != null) {
                                    "\n📍 Localisation : https://www.google.com/maps?q=$latitude,$longitude"
                                } else {
                                    "\n📍 Localisation introuvable"
                                }

                                val message = "Alerte SafeDride 🚨\nFreinage brusque détecté.$locationPart"
                                openSmsIntent(phoneNumber1, phoneNumber2, message)
                            } else {
                                Log.e("INTENT_SMS", "Un ou deux numéros sont manquants")
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("INTENT_SMS", "Erreur Firestore : ${e.message}")
                        }
                }
            }
        }.start()
    }



    private fun openSmsIntent(phone1: String, phone2: String, message: String) {
        val uri = Uri.parse("smsto:$phone1;$phone2")
        val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
            putExtra("sms_body", message)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }




    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        unregisterReceiver(alertDismissReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
