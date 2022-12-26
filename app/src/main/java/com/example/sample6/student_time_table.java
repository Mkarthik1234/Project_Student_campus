package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class student_time_table extends AppCompatActivity {

    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;

    FirebaseAuth auth;
    FirebaseFirestore fstore;

    ImageView timetable_image,download_image;
    TextView textView;
    Bitmap bitmap;
    BitmapDrawable bitmapDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_time_table);

        update_profile();

        navigationView = findViewById(R.id.navigationBar_stutimetable);
        toolbar = findViewById(R.id.toolbar_stutimetable);
        drawerLayout = findViewById(R.id.drawerLayout_stutimetable);

        auth = FirebaseAuth.getInstance();

        timetable_image = findViewById(R.id.timetable_image_stutimetable);
        download_image = findViewById(R.id.download_image_stutimetable);
        textView = findViewById(R.id.download_text_stutimetable);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_menu_menubar:
                    {
                        startActivity(new Intent(student_time_table.this,student_main.class));
                        break;
                    }
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
                        startActivity(new Intent(student_time_table.this, MainActivity.class));
                        finish();
                        break;
                    }
                }
                return false;
            }
        });

        fstore = FirebaseFirestore.getInstance();
        DocumentReference ref = fstore.collection("Class").document("Timetable");
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    Glide.with(student_time_table.this).load(documentSnapshot.getString("timetable1")).into(timetable_image);
                }
            }
        });

        download_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(student_time_table.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(student_time_table.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else
                {
                    download_Image();
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(student_time_table.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(student_time_table.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else
                {
                    download_Image();
                }
            }
        });

    }

    private void update_profile() {
        navigationView = findViewById(R.id.navigationBar_stutimetable);

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
                        Glide.with(student_time_table.this).load(documentSnapshot.getString("profile_image")).into(profile);
                        profile_name.setText(documentSnapshot.getString("Full Name"));
                    }
                }
            }
        });
    }

    public void download_Image()
    {
        bitmapDrawable = (BitmapDrawable) timetable_image.getDrawable();
        bitmap = bitmapDrawable.getBitmap();

        FileOutputStream fileOutputStream=null;
        File sdcard = Environment.getExternalStorageDirectory();
        File Directory = new File(sdcard.getAbsoluteFile()+"/Download");
        Directory.mkdir();

        String filename = String.format("%d.jpg",System.currentTimeMillis());
        File outfile = new File(Directory,filename);

        Toast.makeText(student_time_table.this, "Image downloaded successfully", Toast.LENGTH_SHORT).show();

        try {
            fileOutputStream = new FileOutputStream(outfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outfile));
            sendBroadcast(intent);

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}