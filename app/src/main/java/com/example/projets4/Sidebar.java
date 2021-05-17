package com.example.projets4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.projets4.Admin.Admin;
import com.example.projets4.Patient.Patient;
import com.example.projets4.Patient.PatientProfileFragment;
import com.example.projets4.authentication.Login;
import com.example.projets4.authentication.Logout;
import com.example.projets4.doctor.Doctor;
import com.example.projets4.doctor.DoctorProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Sidebar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_sidebar);
        drawerLayout = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener( this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container, new HomeFragment()).commit();
                break;
            case R.id.profile:
                Bundle extras = getIntent().getExtras();
//                Log.d("TAG", "success navigation to profile ");
//                Log.d("TAG", "extras :  " + extras.getString("isPatient"));
                if (extras.getString("isDoc") != null ){
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container, new DoctorProfileFragment()).commit();
                }

                if (extras.getString("isPatient") != null ){
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container, new PatientProfileFragment()).commit();
                }
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.logout:
                logout();
                Log.d("TAG", "Logged out ");
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}