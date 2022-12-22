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
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login_admin extends AppCompatActivity {
    EditText username,pass;
    TextView signup;
    Button login;
    FirebaseAuth auth;
    FirebaseFirestore fstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        username = findViewById(R.id.edittext_username_adminlogin);
        pass = findViewById(R.id.edittext_password_adminlogin);
        signup = findViewById(R.id.textView_signup_admin);
        login = findViewById(R.id.btn_login_adminlogin);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_admin.this,sign_up.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signInWithEmailAndPassword(username.getText().toString(),pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = auth.getCurrentUser();
                        DocumentReference ref = fstore.collection("Admin").document(user.getUid());
                        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.getString("isAdmin").equals("1"))
                                {

                                    SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("isProfessor","4");
                                    editor.apply();

                                    Toast.makeText(login_admin.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(login_admin.this,admin_HomePage.class));
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(login_admin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_admin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}