package com.example.monapplication.ui.theme

import androidx.activity.ComponentActivity
import com.example.monapplication.R

// Fonction pour appliquer le thème clair uniquement
fun applyTheme(activity: ComponentActivity) {
    // Applique uniquement le thème clair
    val themeId = R.style.AppTheme  // Référence correcte au style AppTheme défini dans themes.xml

    // Applique le thème à l'activité
    activity.setTheme(themeId)
}
