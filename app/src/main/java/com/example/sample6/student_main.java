package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class student_main extends AppCompatActivity {
    FirebaseAuth auth;
    ImageView time_table,subjects,marks,atendese;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        auth = FirebaseAuth.getInstance();

        time_table = findViewById(R.id.img_attendense_stuhome);
        subjects = findViewById(R.id.img_subjects_stuhome);
        marks = findViewById(R.id.img_marks_stuhome);
        atendese = findViewById(R.id.img_attendense_stuhome);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_logout_opmenu:
                auth.signOut();
                removeSharedPreferese("999");
                startActivity(new Intent(student_main.this,student_login.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeSharedPreferese(String no) {
        SharedPreferences ref = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putString("isProfessor",no);
        editor.apply();
    }
}
