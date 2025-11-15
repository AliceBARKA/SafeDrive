package com.example.monapplication.ui.theme.auth


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.monapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var phone1EditText: EditText
    private lateinit var phone2EditText: EditText
    private lateinit var saveButton: Button
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        nameEditText = findViewById(R.id.nameEditText)
        phone1EditText = findViewById(R.id.phone1EditText)
        phone2EditText = findViewById(R.id.phone2EditText)
        saveButton = findViewById(R.id.saveButton)

        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { doc ->
                    nameEditText.setText(doc.getString("name"))
                    phone1EditText.setText(doc.getString("contact1"))
                    phone2EditText.setText(doc.getString("contact2"))
                }
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val phone1 = phone1EditText.text.toString()
            val phone2 = phone2EditText.text.toString()

            if (userId != null && name.isNotEmpty() && phone1.isNotEmpty()) {
                db.collection("users").document(userId)
                    .update(
                        mapOf(
                            "name" to name,
                            "contact1" to phone1,
                            "contact2" to phone2
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profil mis à jour ✅", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erreur ❌", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
