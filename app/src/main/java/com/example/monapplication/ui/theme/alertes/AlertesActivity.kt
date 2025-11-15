package com.example.monapplication.alertes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.monapplication.R

class AlertActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        val okButton = findViewById<Button>(R.id.okButton) // ⬅️ assure-toi que R.id.okButton existe bien
        okButton.setOnClickListener {
            val intent = Intent("ALERT_DISMISSED") // ⬅️ envoie le broadcast
            sendBroadcast(intent)
            finish()
        }
    }
}
