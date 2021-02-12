package com.example.todoappkotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var UID : String

    private var TAG = "MainActivity"
    private lateinit var signInButton: SignInButton
    private lateinit var mAuth: FirebaseAuth

//    private lateinit var logout: Button
    private lateinit var todoTextbox: EditText
    private lateinit var add: Button
    private lateinit var listview: ListView
    private lateinit var loginIntent: Intent

    private lateinit var db: FirebaseFirestore

    private var DATA = mutableListOf<String>()

    private lateinit var ADAPTER : RowAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        supportActionBar?.hide()

        todoTextbox = findViewById(R.id.todoTextbox);
        add = findViewById(R.id.add);
        listview = findViewById(R.id.listView);


        mAuth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        val currentUser = mAuth.currentUser

        loginIntent = Intent(this, LoginActivity::class.java)

        if(currentUser == null) {
            startActivity(loginIntent);
        }else{
            UID = currentUser.uid

//            Log.d(TAG, getString(R.string.logged_as_prefix))
//            userName.text = getString(R.string.logged_as_prefix) + " " + currentUser.displayName

//            actionBar?.title = currentUser.displayName
            title = currentUser.displayName + "'s todo list"

            ADAPTER = RowAdapter(this, DATA, ::onDelete)
            listview.adapter = ADAPTER

            val docRef = db.collection("todo").document(UID)

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
                    DATA.clear()
                    snapshot.data!!.forEach { (key, value) -> DATA.add(value.toString()) }
                    ADAPTER.notifyDataSetChanged()
                } else {
                    Log.d(TAG, "$source data: null")
                }
            }

        }

    }

    private fun onDelete(position : Int) : Int {
        Log.d(TAG, position.toString())
        DATA.removeAt(position)
        updateListViewAndFirestore()
        return position
    }

    fun onAdd(view: View){
        if(todoTextbox.text.isNotEmpty()) {
            DATA.add(todoTextbox.text.toString())
            updateListViewAndFirestore()

            todoTextbox.setText("")
        }else{
            Toast.makeText(this, "Text can't be empty", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateListViewAndFirestore(){
        val map = mutableMapOf<String, String>()

        var i : Int = 0
        for (row in DATA){
            map[i.toString()] = DATA[i]
            i++
        }

        db.collection("todo").document(UID).set(map);
    }

    fun onLogOut(){
        mAuth.signOut();

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(this, gso);
        client.signOut()

        startActivity(loginIntent);
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_logout -> onLogOut()
            R.id.menu_profile -> openProfile()
        }
        return super.onOptionsItemSelected(item)
    }

    fun openProfile(){
        startActivity(Intent(this, ProfileActivity::class.java))
    }
}