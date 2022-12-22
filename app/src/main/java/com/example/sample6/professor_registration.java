package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
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

public class professor_registration extends AppCompatActivity {

    EditText name,email,phone,address,pass,confirmpass;
    ImageButton regstr;

    FirebaseAuth auth;
    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_registration);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.edittext_name_proreg);
        phone = findViewById(R.id.edittext_mobile_proreg);
        email = findViewById(R.id.edittext_email_proreg);
        address = findViewById(R.id.edittext_address_proreg);
        pass = findViewById(R.id.edittext_password_proreg);
        confirmpass = findViewById(R.id.edittext_confirm_password_proreg);
        regstr = findViewById(R.id.imgbtn_register_proreg);

        regstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = auth.getCurrentUser();

                        DocumentReference ref = fstore.collection("Professors").document(user.getUid());
                        HashMap <String,Object> proinfo = new HashMap<>();
                        proinfo.put("Name",name.getText().toString());
                        proinfo.put("Email",email.getText().toString());
                        proinfo.put("Mobile",phone.getText().toString());
                        proinfo.put("Address",address.getText().toString());

                        ref.set(proinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                send_message();
                                Toast.makeText(professor_registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(professor_registration.this,professor_registration.class));

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(professor_registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void send_message() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone.getText().toString(),null,
                "Credentials for Student Portal \nusername : "+email.getText().toString()+" password : "+pass.getText().toString(),
                null,null);
    }
}