package com.example.monapplication.ui.theme.alertes

import java.util.Date

data class DangerAlert(
    val type: String,
    val location: String,
    val details: String,
    val timestamp: Date?
)
