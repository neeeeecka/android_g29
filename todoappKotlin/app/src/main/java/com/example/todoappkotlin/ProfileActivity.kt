package com.example.todoappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile2.*

//CODE WRITTEN BY NIKOLOZ GABISONIA

class ProfileActivity : AppCompatActivity() {

    private lateinit var UID : String

    private lateinit var mAuth: FirebaseAuth
    private lateinit var userName: TextView
    private lateinit var db: FirebaseFirestore

    private lateinit var nameTextBox: EditText
    private lateinit var ageTextBox: EditText

    private var TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile2)

        nameTextBox = findViewById(R.id.name_box);
        ageTextBox = findViewById(R.id.age_box);


        mAuth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        val currentUser = mAuth.currentUser

        val loginIntent = Intent(this, LoginActivity::class.java)

        if(currentUser == null) {
            startActivity(loginIntent);
        }else{
            UID = currentUser.uid

            title = currentUser.displayName + "'s profile"

            val docRef = db.collection("users").document(UID)
            docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val source = if (snapshot != null && snapshot.metadata.hasPendingWrites())
                    "Local"
                else
                    "Server"

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "$source data: ${snapshot.data}")
                    nameTextBox.setText(snapshot.data?.get("name").toString())
                    ageTextBox.setText(snapshot.data?.get("age").toString())
                } else {
                    Log.d(TAG, "$source data: null")
                }
            }
        }

    }

    fun onSave(view: View){
        val map = mapOf<String, String>("name" to nameTextBox.text.toString(), "age" to ageTextBox.text.toString())
        Log.d(TAG, map.toString())
        db.collection("users").document(UID).set(map);
        Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
    }
}