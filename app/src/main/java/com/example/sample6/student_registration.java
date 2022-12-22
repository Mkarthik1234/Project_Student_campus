package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class student_registration extends AppCompatActivity {

    EditText usn, name, mob, pass;
    ImageButton regstr;

    FirebaseAuth auth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        usn = findViewById(R.id.edittext_email_stureg);
        name = findViewById(R.id.edittext_name_stureg);
        mob = findViewById(R.id.edittext_mobile_stureg);
        pass = findViewById(R.id.edittext_password_stureg);
        regstr = findViewById(R.id.imgbtn_register);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        regstr.setOnClickListener(view -> {
            String u, n, m, p;
            u = usn.getText().toString();
            n = name.getText().toString();
            m = mob.getText().toString();
            p = pass.getText().toString();
            if (u.isEmpty() || n.isEmpty() || m.isEmpty() || p.isEmpty())
                Toast.makeText(student_registration.this, "Fill all the details", Toast.LENGTH_SHORT).show();
            else {
                auth.createUserWithEmailAndPassword(u, p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser usr = auth.getCurrentUser();

                        DocumentReference ref = fstore.collection("Students").document(usr.getUid());

                        HashMap<String, Object> stuinfo = new HashMap<>();
                        stuinfo.put("Full Name", n);
                        stuinfo.put("Email", u);
                        stuinfo.put("Mobile", m);
                        stuinfo.put("isProfessor", "0");
                        ref.set(stuinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                send_message();
                                Toast.makeText(student_registration.this, "registration successful", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(student_registration.this, student_registration.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(student_registration.this, "registration Failed2", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(student_registration.this, "registration Failed1", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void send_message() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mob.getText().toString(), null, "Credentials for Student Portal\n username : " + usn.getText().toString() + " password : " + pass.getText().toString(), null, null);
        Log.d("inside", "true");
    }
}