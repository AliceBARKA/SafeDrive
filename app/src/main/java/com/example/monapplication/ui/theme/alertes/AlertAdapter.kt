package com.example.monapplication.ui.theme.alertes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.monapplication.R
import java.text.SimpleDateFormat
import java.util.*

class AlertAdapter(private val alertList: List<DangerAlert>) :
    RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alert, parent, false)
        return AlertViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = alertList[position]
        holder.typeText.text = "Type : ${alert.type}"
        holder.locationText.text = "Lieu : ${alert.location}"
        holder.detailsText.text = "Détails : ${alert.details}"

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.dateText.text = "Date : ${alert.timestamp?.let { sdf.format(it) } ?: "Inconnue"}"
    }

    override fun getItemCount(): Int = alertList.size

    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeText: TextView = itemView.findViewById(R.id.alertTypeText)
        val locationText: TextView = itemView.findViewById(R.id.alertLocationText)
        val detailsText: TextView = itemView.findViewById(R.id.alertDetailsText)
        val dateText: TextView = itemView.findViewById(R.id.alertDateText)
    }
}
