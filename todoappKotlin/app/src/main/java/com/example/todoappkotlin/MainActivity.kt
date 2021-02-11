package com.example.todoappkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var TAG = "MainActivity"
    private lateinit var signInButton: SignInButton
    private lateinit var mAuth: FirebaseAuth
//    private lateinit var currentUser: FirebaseUser

    private lateinit var userName: TextView
    private lateinit var logout: Button
    private lateinit var todoTextbox: EditText
    private lateinit var add: Button
    private lateinit var listview: ListView
    private lateinit var loginIntent: Intent

    private lateinit var db: FirebaseFirestore

    private var data: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        userName = findViewById(R.id.userName);
        logout = findViewById(R.id.logout);
        todoTextbox = findViewById(R.id.todoTextbox);
        add = findViewById(R.id.add);
        listview = findViewById(R.id.listView);

        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth.currentUser

        loginIntent = Intent(this, LoginActivity::class.java)

        if(currentUser == null) {
            startActivity(loginIntent);
        }else{
            userName.text = "Logged as: " + currentUser.displayName

        }

    }
    fun onAdd(view: View){

    }
    fun onLogOut(view: View){
        mAuth.signOut();

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(this, gso);
        client.signOut()

        startActivity(loginIntent);
    }
}