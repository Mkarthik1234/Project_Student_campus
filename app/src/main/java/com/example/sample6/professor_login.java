package com.example.sample6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class professor_login extends AppCompatActivity {
    Button login;
    TextView signup;
    EditText username,password;

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_login);

        login = findViewById(R.id.btn_login);
        signup = findViewById(R.id.textView_signup);
        username = findViewById(R.id.edittext_username);
        password = findViewById(R.id.edittext_password);

        db = new Database(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity_professorHome();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String u,p;
                u = username.getText().toString();
                p = password.getText().toString();

                if(u.isEmpty() || p.isEmpty())
                    Toast.makeText(professor_login.this, "Enter all Fields", Toast.LENGTH_SHORT).show();
                else
                {
                    Boolean res = db.check_professor_login(u,p);
                    if(res==true)
                    {
                        Toast.makeText(professor_login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity_studentRegistration();
                    }
                    else
                        Toast.makeText(professor_login.this, "Enter valid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startActivity_professorHome() {
        Intent intent = new Intent(this.getApplicationContext(),sign_up.class);
        startActivity(intent);
    }

    private void startActivity_studentRegistration() {
        Intent intent = new Intent(this.getApplicationContext(),Student_registration.class);
        startActivity(intent);
    }
}