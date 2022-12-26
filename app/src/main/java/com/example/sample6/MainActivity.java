package com.example.sample6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button start;
    TextView welcome,to;

    private static int splash_timeout=5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcome = findViewById(R.id.welcome_textView);
        to = findViewById(R.id.welcome_to_textView);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.SEND_SMS},1);
                }
            }
        },2000);

        start = findViewById(R.id.btn_getstarted);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(),select_user.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_in,R.anim.right_slide_out);
            }
        });

        Animation myanimation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.animation1);
        welcome.startAnimation(myanimation);

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
        else if(res.equals("4"))
        {
            Toast.makeText(this, "You are Admin", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,admin_HomePage.class));
        }
    }
}