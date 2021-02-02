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
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//import

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    private TextView userName;
    private Button logout;
    private EditText todoTextbox;
    private Button add;
    private ListView listview;

    Intent loginIntent;
    FirebaseFirestore db;

    String[] data = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        logout = findViewById(R.id.logout);
        todoTextbox = findViewById(R.id.todoTextbox);
        add = findViewById(R.id.add);
        listview = findViewById(R.id.listView);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        loginIntent = new Intent(this, LoginActivity.class);

        if(currentUser == null) {
            startActivity(loginIntent);
        }else{
            userName.setText("Logged as: " + currentUser.getDisplayName());
        }

        db = FirebaseFirestore.getInstance();

        String uid = currentUser.getUid();

        DocumentReference docRef = db.collection("todo").document("black");
        listview.setAdapter(new ListElementAdapter(this, data));

    }

//    public T myMethod(Callable<T> func) {
//        return func.call();
//    }

    public void onLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(loginIntent);
    }

    public void onAdd(View view){
        String[] data = new String[]{ "data1",
                "data", "data3" };
        listview.setAdapter(new ListElementAdapter(this, data));

        Map<String, Object> docData = new HashMap<>();
        docData.put("name", "Los Angeles");
        // Add a new document (asynchronously) in collection "cities" with id "LA"
        ApiFuture<WriteResult> future = (ApiFuture<WriteResult>) db.collection("todo").document("black").set(docData);

    }


}