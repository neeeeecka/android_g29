package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

//import

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    private TextView userName;
    private Button logout;
    private EditText todoTextbox;
    private Button add;

    Intent loginIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        logout = findViewById(R.id.logout);
        todoTextbox = findViewById(R.id.todoTextbox);
        add = findViewById(R.id.add);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        loginIntent = new Intent(this, LoginActivity.class);

        if(currentUser == null) {
            startActivity(loginIntent);
        }else{
            userName.setText("Logged as: " + currentUser.getDisplayName());
        }

        ListView  listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(new ListElementAdapter(this, new String[] { "data1",
                "data2" }));

    }

    public void onLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(loginIntent);
    }

    public void onAdd(View view){

    }


}