package com.example.sample6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class select_user extends AppCompatActivity {
    ImageButton student,professor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        student = findViewById(R.id.imgbtn_student);
        professor = findViewById(R.id.imgbtn_professor);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity_studentlogin();
            }
        });
        professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity_professorlogin();
            }
        });
    }

    private void startActivity_studentlogin() {
        Intent intent = new Intent(this,student_login.class);
        startActivity(intent);
    }
    private void startActivity_professorlogin() {
        Intent intent = new Intent(this,professor_login.class);
        startActivity(intent);
    }
}