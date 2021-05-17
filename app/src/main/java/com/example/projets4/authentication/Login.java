package com.example.projets4.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import android.*;
import com.example.projets4.Admin.Admin;
import com.example.projets4.HomeFragment;
import com.example.projets4.MainActivity;
import com.example.projets4.Patient.Patient;
import com.example.projets4.R;
import com.example.projets4.Sidebar;
import com.example.projets4.doctor.Doctor;
import com.example.projets4.doctor.Doctor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText loginEmail, loginPassword;
    Button loginBtn, createAccountBtn;
    boolean valid = true;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        createAccountBtn = findViewById(R.id.createAccBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginEmail.getText().toString().isEmpty()){
                    loginEmail.setError("Email is missing!!");
                    return;
                }

                if(loginPassword.getText().toString().isEmpty()){
                    loginPassword.setError("Password is missing!!");
                    return;
                }

                fAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkUserAccessLevel(authResult.getUser().getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        // extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.getString("isDoc") != null){
                    Log.d("TAG", "user Role!!: " + documentSnapshot.getData());
                    Intent intent = new Intent(getApplicationContext(), Sidebar.class);
                    intent.putExtra("isDoc", "1");
                    getApplicationContext().startActivity(intent);
                    finish();
                }

                if(documentSnapshot.getString("isAdmin") != null){
                    Intent intent = new Intent(getApplicationContext(), Sidebar.class);
                    intent.putExtra("isAdmin", "1");
                    getApplicationContext().startActivity(intent);
                    finish();
                }

                if(documentSnapshot.getString("isPatient") != null){
                    Intent intent = new Intent(getApplicationContext(), Sidebar.class);
                    intent.putExtra("isPatient", "1");
                    getApplicationContext().startActivity(intent);
                    finish();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() !=null){
            DocumentReference df = fStore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.getString("isDoc") != null){
                        Intent intent = new Intent(getApplicationContext(), Sidebar.class);
                        intent.putExtra("isDoc", "1");
                        getApplicationContext().startActivity(intent);
                        finish();
                    }

                    if(documentSnapshot.getString("isAdmin") != null){
                        Intent intent = new Intent(getApplicationContext(), Sidebar.class);
                        intent.putExtra("isAdmin", "1");
                        getApplicationContext().startActivity(intent);
                        finish();
                    }

                    if(documentSnapshot.getString("isPatient") != null){
                        Intent intent = new Intent(getApplicationContext(), Sidebar.class);
                        intent.putExtra("isPatient", "1");
                        getApplicationContext().startActivity(intent);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });
        }

    }
}