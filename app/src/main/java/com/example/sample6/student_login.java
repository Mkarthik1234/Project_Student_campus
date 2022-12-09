package com.example.sample6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class student_login extends AppCompatActivity {
    Button login;
    EditText username,pass;
    SharedPreferences sharedPreferences;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        username = findViewById(R.id.editText_username_student);
        pass = findViewById(R.id.edittext_password_student);
        login = findViewById(R.id.btn_login_student);

        db = new Database(this);
        sharedPreferences = getSharedPreferences("student_login",MODE_PRIVATE);

        login.setOnClickListener(view -> {
            String u,p;
            u = username.getText().toString();
            p = pass.getText().toString();

            if(u.isEmpty() || p.isEmpty())
                Toast.makeText(student_login.this, "Enter all fields", Toast.LENGTH_SHORT).show();
            else
            {
                boolean res = db.check_student_login(u,p);
                if(res)
                {
                    Toast.makeText(student_login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    //startActivity_studentHome();
                    //SharedPreferences.Editor editor = SharedPreferences;

                }
                else
                    Toast.makeText(student_login.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startActivity_studentHome() {
        Intent intent = new Intent(this.getApplicationContext(),Student_registration.class);
        startActivity(intent);
    }
}