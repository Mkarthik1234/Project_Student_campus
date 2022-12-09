package com.example.sample6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Student_registration extends AppCompatActivity {

    EditText usn,name,mob,pass;
    ImageButton regstr;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        usn = findViewById(R.id.edittext_usn);
        name = findViewById(R.id.edittext_name);
        mob = findViewById(R.id.edittext_mobile);
        pass = findViewById(R.id.edittext_password);
        regstr = findViewById(R.id.imgbtn_register);
        db = new Database(this);
        regstr.setOnClickListener(view -> {
            String u, n, m, p;
            u = usn.getText().toString();
            n = name.getText().toString();
            m = mob.getText().toString();
            p = pass.getText().toString();
            if (u.isEmpty() || n.isEmpty() || m.isEmpty() || p.isEmpty())
                Toast.makeText(Student_registration.this, "Fill all the details", Toast.LENGTH_SHORT).show();
            else
            {
                boolean res = db.register_student(u, n, m, p);
                if (res)
                {
                    Toast.makeText(Student_registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Student_registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}