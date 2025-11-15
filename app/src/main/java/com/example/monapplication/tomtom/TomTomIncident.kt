package com.example.monapplication.tomtom

data class TomTomIncident(
    val category: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val documentId: String? = null // null pour incidents TomTom

)
