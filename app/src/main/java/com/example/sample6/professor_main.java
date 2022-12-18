package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.zip.Inflater;

public class professor_main extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_main);

        auth = FirebaseAuth.getInstance();
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
                finish();
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