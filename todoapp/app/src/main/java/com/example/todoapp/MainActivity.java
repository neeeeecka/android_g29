package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityCheck";
    private FirebaseAuth mAuth;

    private TextView userName;
    private Button logout;
    private EditText todoTextbox;
    private Button add;
    private ListView listview;

    Intent loginIntent;
    FirebaseFirestore db;

    List<String> data = new ArrayList<String>();


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

        final Context context = this;

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    data.clear();
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    for (Object value : snapshot.getData().values()) {
//                        Log.d(TAG, "Current data: " + value);
                        data.add(value + "");
                    }
                    String[] buffer = new String[ data.size() ];
                    listview.setAdapter(new ListElementAdapter(context, data.toArray(buffer)));

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }

//    public T myMethod(Callable<T> func) {
//        return func.call();
//    }

    public void onLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(loginIntent);
    }

    public void onAdd(View view){
        data.add(todoTextbox.getText() + "");

        String[] buffer = new String[ data.size() ];
        listview.setAdapter(new ListElementAdapter(this, data.toArray(buffer)));

        Map<String, Object> dataMap = new HashMap<>();

        for(int i = 0; i < data.size(); i++){
            dataMap.put(i+"", data.get(i));
        }

//        db.collection("todo")
//                .add(dataMap)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
        db.collection("todo").document("black").set(dataMap);
    }


}