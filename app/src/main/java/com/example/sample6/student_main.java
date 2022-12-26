package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class student_main extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    ImageView time_table, subjects, marks, atendese;

    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        update_profile();

        auth = FirebaseAuth.getInstance();

        time_table = findViewById(R.id.img_time_table_stuhome);
        subjects = findViewById(R.id.img_subjects_stuhome);
        marks = findViewById(R.id.img_marks_stuhome);
        atendese = findViewById(R.id.img_attendense_stuhome);

        navigationView = findViewById(R.id.navigationBar_studentMain);
        toolbar = findViewById(R.id.toolbar_studentMain);
        drawerLayout = findViewById(R.id.drawerLayout_studentMain);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_menu_menubar:
                        break;
                    case R.id.exit_menu_menubar: {
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                        break;
                    }
                    case R.id.logout_menubar: {
                        auth.signOut();
                        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("isProfessor", "999");
                        editor.apply();
                        startActivity(new Intent(student_main.this, MainActivity.class));
                        finish();
                        break;
                    }
                }
                return false;
            }
        });



        time_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(student_main.this,student_time_table.class));
            }
        });

        subjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(student_main.this,student_subjects.class));
            }
        });

        atendese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(student_main.this,student_attendense_subjectView.class));
            }
        });
    }

    private void update_profile() {
        navigationView = findViewById(R.id.navigationBar_studentMain);

        View header = navigationView.getHeaderView(0);
        ImageView profile = (ImageView) header.findViewById(R.id.profile_image_menuBarHeader);
        TextView profile_name = (TextView) header.findViewById(R.id.profileName_menuBarHeader);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        DocumentReference ref = fstore.collection("Students").document(user.getUid());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (!documentSnapshot.getString("profile_image").isEmpty() && !documentSnapshot.getString("Full Name").isEmpty()) {
                        Glide.with(student_main.this).load(documentSnapshot.getString("profile_image")).into(profile);
                        profile_name.setText(documentSnapshot.getString("Full Name"));
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_logout_opmenu:
                auth.signOut();
                removeSharedPreferese("999");
                startActivity(new Intent(student_main.this, student_login.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeSharedPreferese(String no) {
        SharedPreferences ref = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putString("isProfessor", no);
        editor.apply();
    }
}
