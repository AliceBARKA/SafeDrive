package com.example.monapplication.ui.theme.alertes



import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.monapplication.R
import com.google.firebase.firestore.FirebaseFirestore

class EditDangerActivity : AppCompatActivity() {

    private lateinit var typeEditText: EditText
    private lateinit var detailsEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var db: FirebaseFirestore
    private var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_danger)

        typeEditText = findViewById(R.id.typeEditText)
        detailsEditText = findViewById(R.id.detailsEditText)
        saveButton = findViewById(R.id.saveButton)

        db = FirebaseFirestore.getInstance()
        documentId = intent.getStringExtra("documentId")

        if (documentId != null) {
            db.collection("dangers").document(documentId!!).get()
                .addOnSuccessListener { doc ->
                    typeEditText.setText(doc.getString("type"))
                    detailsEditText.setText(doc.getString("details"))
                }
        }

        saveButton.setOnClickListener {
            val type = typeEditText.text.toString()
            val details = detailsEditText.text.toString()

            if (documentId != null && type.isNotEmpty()) {
                db.collection("dangers").document(documentId!!)
                    .update("type", type, "details", details)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Modifié ✅", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erreur ❌", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
