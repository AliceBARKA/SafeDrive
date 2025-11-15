package com.example.monapplication.ui.theme.alertes


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class AlertDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ALERT", "Alerte annulée par l'utilisateur")

        // Permet de réinitialiser le flag dans BrakeDetectionService via un intent
        val resetIntent = Intent("ALERT_DISMISSED")
        context.sendBroadcast(resetIntent)
    }
}
