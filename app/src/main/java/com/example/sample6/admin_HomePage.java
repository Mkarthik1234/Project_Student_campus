package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class admin_HomePage extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;

    ImageButton sturegister, proregister;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar1);

        sturegister = findViewById(R.id.imageButton_student_adminhome);
        proregister = findViewById(R.id.imageButton_professor_adminhome);


        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        DocumentReference ref = fstore.collection("Students").document(user.getUid());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sturegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_HomePage.this, student_registration.class));
            }
        });
        proregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_HomePage.this, professor_registration.class));
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_menu_menubar: {
                        startActivity(new Intent(admin_HomePage.this, admin_HomePage.class));
                        break;
                    }
                    case R.id.logout_menubar: {
                        auth.signOut();
                        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("isProfessor", "999");
                        editor.apply();
                        startActivity(new Intent(admin_HomePage.this, MainActivity.class));
                        finish();
                        break;
                    }
                    case R.id.exit_menu_menubar: {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                        break;
                    }
                }
                return true;
            }
        });
        navigationView.bringToFront();
    }

//    @Override
//    protected void onStart() {
//        FirebaseUser user = auth.getCurrentUser();
//        DocumentReference ref = fstore.collection("Admin").document(user.getUid());
//        View header = navigationView.getHeaderView(0);
//        profile =(ImageView) header.findViewById(R.id.profile_image_menuBarHeader);
//        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists() && !documentSnapshot.getString("profile_image").isEmpty())
//                {
//                    Glide.with(admin_HomePage.this).load(documentSnapshot.getString("profile_image")).timeout(6000).into(profile);
//                }
//            }
//        });
//        super.onStart();
//    }
}