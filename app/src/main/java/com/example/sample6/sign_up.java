package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;

public class sign_up extends AppCompatActivity {
    EditText email,pass1,pass2;
    Button sign_up;

    FirebaseAuth auth;
    FirebaseFirestore fstore;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        email = findViewById(R.id.edittext_email);
        pass1 = findViewById(R.id.edittext_password);
        pass2 = findViewById(R.id.edittext_confirm_password);
        sign_up = findViewById(R.id.btn_sign_up);


        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e,p1,p2;
                e = email.getText().toString();
                p1 = pass1.getText().toString();
                p2 = pass2.getText().toString();
                
                if(e.isEmpty() || p1.isEmpty() || p2.isEmpty())
                    Toast.makeText(sign_up.this, "Enter all details", Toast.LENGTH_SHORT).show();
                else
                {
                    if(!p1.equals(p2))
                        Toast.makeText(sign_up.this, "Please enter correct password", Toast.LENGTH_SHORT).show();
                    else
                    {
//                        Boolean res = db.register_professor(e,p1);
//                        if(res == true)
//                        {
//                            Toast.makeText(sign_up.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                            Toast.makeText(sign_up.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();

                          auth.createUserWithEmailAndPassword(e,p1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                              @Override
                              public void onSuccess(AuthResult authResult) {
                                  FirebaseUser usr = auth.getCurrentUser();

                                  DocumentReference dref = fstore.collection("Professors").document(usr.getUid());

                                  HashMap <String,Object> proinfo = new HashMap<>();
                                  proinfo.put("Email",e);
                                  proinfo.put("isProfessor","1");

                                  dref.set(proinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                      @Override
                                      public void onSuccess(Void unused) {
                                          Toast.makeText(sign_up.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                          startActivity(new Intent(sign_up.this,professor_login.class));
                                          finish();
                                      }
                                  }).addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull Exception e) {
                                          Toast.makeText(sign_up.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                                      }
                                  });


                              }
                          });


                     }
                }
            }
        });
        
    }

}