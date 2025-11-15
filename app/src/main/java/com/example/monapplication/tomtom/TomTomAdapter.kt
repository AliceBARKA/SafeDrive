package com.example.monapplication.tomtom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.monapplication.R

class TomTomAdapter(
    private val incidents: List<TomTomIncident>,
    private val onDeleteClicked: (String) -> Unit,
    private val onEditClicked: (TomTomIncident) -> Unit
) : RecyclerView.Adapter<TomTomAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryText: TextView = itemView.findViewById(R.id.incidentCategory)
        val descriptionText: TextView = itemView.findViewById(R.id.incidentDescription)
        val coordsText: TextView = itemView.findViewById(R.id.incidentCoords)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val editButton: Button = itemView.findViewById(R.id.editButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_incident, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val incident = incidents[position]
        holder.categoryText.text = incident.category
        holder.descriptionText.text = incident.description
        holder.coordsText.text = "(${incident.latitude}, ${incident.longitude})"

        if (incident.documentId != null) {
            holder.deleteButton.visibility = View.VISIBLE
            holder.editButton.visibility = View.VISIBLE

            holder.deleteButton.setOnClickListener {
                onDeleteClicked(incident.documentId)
            }

            holder.editButton.setOnClickListener {
                onEditClicked(incident)
            }
        } else {
            holder.deleteButton.visibility = View.GONE
            holder.editButton.visibility = View.GONE
        }


    }

    override fun getItemCount() = incidents.size


}
