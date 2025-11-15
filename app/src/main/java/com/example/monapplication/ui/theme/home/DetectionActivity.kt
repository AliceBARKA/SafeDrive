package com.example.monapplication.ui.theme.home


import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import com.example.monapplication.R
import com.example.monapplication.ui.service.BrakeDetectionService

class DetectionActivity : AppCompatActivity() {

    private lateinit var chronometer: Chronometer
    private lateinit var stopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detection)

        chronometer = findViewById(R.id.chronometer)
        stopButton = findViewById(R.id.stopButton)

        // Lancer le chrono
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        // Lancer le service de détection
        val intent = Intent(this, BrakeDetectionService::class.java)
        startService(intent)

        stopButton.setOnClickListener {
            stopService(intent) // Stoppe la détection
            finish() // Ferme cette page
        }
    }
}
