package com.example.todoappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var userName: TextView
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile2)

        mAuth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        val currentUser = mAuth.currentUser

        val loginIntent = Intent(this, LoginActivity::class.java)

        if(currentUser == null) {
            startActivity(loginIntent);
        }else{
            title = currentUser.displayName + "'s profile"
        }

    }
}