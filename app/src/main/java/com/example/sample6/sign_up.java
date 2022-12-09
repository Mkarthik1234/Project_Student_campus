package com.example.sample6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class sign_up extends AppCompatActivity {
    EditText email,pass1,pass2;
    Button sign_up;
    
    Database db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        email = findViewById(R.id.edittext_email);
        pass1 = findViewById(R.id.edittext_password);
        pass2 = findViewById(R.id.edittext_confirm_password);
        sign_up = findViewById(R.id.btn_sign_up);
        
        db = new Database(this);
        
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
                        Boolean res = db.register_professor(e,p1);
                        if(res == true)
                        {
                            Toast.makeText(sign_up.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(sign_up.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        
    }
}