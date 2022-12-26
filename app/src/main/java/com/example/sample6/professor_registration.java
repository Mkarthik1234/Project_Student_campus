package com.example.sample6;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.security.acl.Permission;
import java.util.Date;
import java.util.HashMap;

public class professor_registration extends AppCompatActivity {

    EditText name,email,phone,address,pass,confirmpass;
    ImageButton regstr;
    ImageView profile,add_image;

    FirebaseAuth auth;
    FirebaseFirestore fstore;
    FirebaseStorage firebaseStorage;

    Uri filepath;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_registration);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        name = findViewById(R.id.edittext_name_proreg);
        phone = findViewById(R.id.edittext_mobile_proreg);
        email = findViewById(R.id.edittext_email_proreg);
        address = findViewById(R.id.edittext_address_proreg);
        pass = findViewById(R.id.edittext_password_proreg);
        confirmpass = findViewById(R.id.edittext_confirm_password_proreg);
        regstr = findViewById(R.id.imgbtn_register_proreg);
        profile = findViewById(R.id.imageView_profile_profreg);
        add_image=findViewById(R.id.add_profileimage_proreg);

        regstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_professor(filepath);
            }
        });
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(professor_registration.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(professor_registration.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent,"Select application for Image"),1);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 || resultCode == RESULT_OK)
        {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profile.setImageBitmap(bitmap);
                }catch (Exception ex)
            {}
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void send_message() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone.getText().toString(),null,
                "Credentials for Student Portal \nusername : "+email.getText().toString()+" password : "+pass.getText().toString(),
                null,null);
    }

    private  void register_professor(Uri filepath)
    {   Log.d("email",email.getText().toString());
        Log.d("pass",pass.getText().toString());
        auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                final StorageReference storageReference = firebaseStorage.getReference("Image"+new Date());
                try{storageReference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseUser user = auth.getCurrentUser();

                                DocumentReference ref = fstore.collection("Professors").document(user.getUid());
                                HashMap <String,Object> proinfo = new HashMap<>();
                                proinfo.put("Name",name.getText().toString());
                                proinfo.put("Email",email.getText().toString());
                                proinfo.put("Mobile",phone.getText().toString());
                                proinfo.put("Address",address.getText().toString());
                                proinfo.put("Profile_image",uri.toString());
                                ref.set(proinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        send_message();
                                        Toast.makeText(professor_registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(professor_registration.this,professor_registration.class));
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                });
            }catch (IllegalArgumentException exception) {
                    Toast.makeText(professor_registration.this, "Select profile image", Toast.LENGTH_SHORT).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(professor_registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}