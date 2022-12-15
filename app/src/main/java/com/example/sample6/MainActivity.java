package com.example.sample6;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
//                String res = pref.getString("isProfessor", "3");
//                if (res.equals("1")) {
//                    startActivity(new Intent(MainActivity.this, professor_main.class));
//                } else if (res.equals("0")) {
//                    startActivity(new Intent(MainActivity.this, student_main.class));
//                }
//            }
//        },4000);

        start = findViewById(R.id.btn_getstarted);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),select_user.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
        String res = pref.getString("isProfessor", "3");
        if (res.equals("1")) {
            Toast.makeText(this, "You are professor", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, professor_main.class));
        } else if (res.equals("0")) {
            Toast.makeText(this, "You are student", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, student_main.class));
        }
    }
}