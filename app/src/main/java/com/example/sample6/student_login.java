package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class student_login extends AppCompatActivity {
    Button login;
    EditText username,pass;
    SharedPreferences sharedPreferences;

    FirebaseAuth auth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        username = findViewById(R.id.editText_username_student);
        pass = findViewById(R.id.edittext_password_student);
        login = findViewById(R.id.btn_login_student);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        login.setOnClickListener(view -> {
            String u,p;
            u = username.getText().toString();
            p = pass.getText().toString();

            if(u.isEmpty() || p.isEmpty())
                Toast.makeText(student_login.this, "Enter all fields", Toast.LENGTH_SHORT).show();
            else
            {
                auth.signInWithEmailAndPassword(u,p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser usr = auth.getCurrentUser();
                        DocumentReference ref = fstore.collection("Students").document(usr.getUid());
                        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists() && documentSnapshot.getString("isProfessor").equals("0"))
                                {
                                    Toast.makeText(student_login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    addtoSharedPreferece(documentSnapshot.getString("isProfessor"));
                                    startActivity_studentHome();
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(student_login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(student_login.this, "login failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void addtoSharedPreferece(String isP) {
        SharedPreferences ref = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putString("isProfessor",isP);
        editor.apply();
    }

    private void startActivity_studentHome() {
        Intent intent = new Intent(this.getApplicationContext(),student_main.class);
        startActivity(intent);
    }
}