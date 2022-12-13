package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    TextView signup;
    EditText username,password;

    FirebaseAuth auth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_login);

        login = findViewById(R.id.btn_login);
        signup = findViewById(R.id.textView_signup);
        username = findViewById(R.id.edittext_username);
        password = findViewById(R.id.edittext_password);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

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
                                        finish();
                                        startActivity(new Intent(professor_login.this,Student_registration.class));
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

    private void startActivity_professorHome() {
        Intent intent = new Intent(this.getApplicationContext(),sign_up.class);
        startActivity(intent);
    }

    private void startActivity_studentRegistration() {
        Intent intent = new Intent(this.getApplicationContext(),Student_registration.class);
        startActivity(intent);
    }
}