package com.example.projets4.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projets4.MainActivity;
import com.example.projets4.Patient.Patient;
import com.example.projets4.R;
import com.example.projets4.doctor.doctor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText registerFirstName, registerLastName, registerEmail, registerPassword, registerConfirmPassword;
    CheckBox isDoctorBox, isPatientBox;
    Button haveAccountBtn, registerBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        registerFirstName = findViewById(R.id.firstName);
        registerLastName = findViewById(R.id.lastName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.confirmPassword);
        isDoctorBox = findViewById(R.id.DocCheckBox);
        isPatientBox = findViewById(R.id.PatientCheckBox);
        registerBtn = findViewById(R.id.registerBtn);
        haveAccountBtn = findViewById(R.id.haveAccBtn);

        // Checkboxes logic
        isPatientBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    isDoctorBox.setChecked(false);
                }
            }
        });

        isDoctorBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    isPatientBox.setChecked(false);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = registerFirstName.getText().toString();
                String lastName = registerLastName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String confirmPassword = registerConfirmPassword.getText().toString();

                if (firstName.isEmpty()){
                    registerFirstName.setError("First Name is Required!!");
                    return;
                }

                if (lastName.isEmpty()){
                    registerLastName.setError("Last Name is Required!!");
                    return;
                }

                if (email.isEmpty()){
                    registerEmail.setError("Email is Required!!");
                    return;
                }

                if (password.isEmpty()){
                    registerPassword.setError("Password is Required!!");
                    return;
                }

                if (confirmPassword.isEmpty()){
                    registerConfirmPassword.setError("Confirming password is Required!!");
                    return;
                }

                if (!password.equals(confirmPassword)){
                    registerConfirmPassword.setError("Password do not Match!!");
                    return;
                }

                if (!(isDoctorBox.isChecked() || isPatientBox.isChecked())){
                    Toast.makeText(Register.this, "Select the Account type", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(Register.this, "Data Validated", Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = fAuth.getCurrentUser();
                        DocumentReference df = fStore.collection("Users").document(user.getUid());
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("FirstName", firstName);
                        userInfo.put("LastName", lastName);
                        userInfo.put("Email", email);

                        //specify user role
                        if (isDoctorBox.isChecked()){
                            userInfo.put("isDoc", "1");
                        }

                        if (isPatientBox.isChecked()){
                            userInfo.put("isPatient", "1");
                        }
                        df.set(userInfo).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });;

                        if (isDoctorBox.isChecked()){
                            startActivity(new Intent(getApplicationContext(), doctor.class));
                            finish();                        }

                        if (isPatientBox.isChecked()){
                            startActivity(new Intent(getApplicationContext(), Patient.class));
                            finish();                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        haveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}