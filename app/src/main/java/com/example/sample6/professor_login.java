package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class professor_login extends AppCompatActivity {
    Button login;
    EditText username,password;

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_login);

        login = findViewById(R.id.btn_login_prologin);
        username = findViewById(R.id.edittext_username_prologin);
        password = findViewById(R.id.edittext_password_prologin);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();



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
                    auth.signInWithEmailAndPassword(u,p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser usr = auth.getCurrentUser();
                            DocumentReference ref = firestore.collection("Professors").document(usr.getUid());
                            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists() && documentSnapshot.getString("isProfessor").equals("1"))
                                    {
                                        Toast.makeText(professor_login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        addtoSharedpreference(documentSnapshot.getString("isProfessor"));
                                        finish();
                                        startActivity_professorHome();
                                    }
                                    else
                                    {
                                        Toast.makeText(professor_login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(professor_login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void addtoSharedpreference(String isP) {
        SharedPreferences ref = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putString("isProfessor",isP);
        editor.apply();
    }

    private void startActivity_professorHome() {
        Intent intent = new Intent(this.getApplicationContext(),professor_main.class);
        startActivity(intent);
    }

    private void startActivity_studentRegistration() {
        Intent intent = new Intent(this.getApplicationContext(),student_main.class);
        startActivity(intent);
    }
}