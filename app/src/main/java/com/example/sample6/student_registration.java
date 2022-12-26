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
import java.util.HashMap;
import java.util.Random;

public class student_registration extends AppCompatActivity {

    EditText usn,email, name, mob, pass;
    ImageButton regstr;
    ImageView add_image, image;
    String admin_user = "IcvXixgQcWU2mVP17C7K65aydbT2";

    Uri filepath;
    Bitmap bitmap;

    FirebaseAuth auth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        usn = findViewById(R.id.edittext_usn_stureg);
        email = findViewById(R.id.edittext_email_stureg);
        name = findViewById(R.id.edittext_name_stureg);
        mob = findViewById(R.id.edittext_mobile_stureg);
        pass = findViewById(R.id.edittext_password_stureg);
        regstr = findViewById(R.id.imgbtn_register);
        image = findViewById(R.id.imageView3_stureg);
        add_image = findViewById(R.id.add_image);

        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(student_registration.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(student_registration.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Image File"), 1);
                }
            }
        });

        regstr.setOnClickListener(view -> {
            register_student(filepath);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 || resultCode == RESULT_OK) {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);
                Log.d("filepath",filepath.toString());
            } catch (Exception ex) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void send_message() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mob.getText().toString(), null, "Credentials for Student Portal\n username : " + usn.getText().toString() + " password : " + pass.getText().toString(), null, null);
        Log.d("inside", "true");
    }

    private void register_student(Uri filepath) {
        String e,u, n, m, p;
        u = usn.getText().toString();
        e = email.getText().toString();
        n = name.getText().toString();
        m = mob.getText().toString();
        p = pass.getText().toString();
        if (u.isEmpty() || n.isEmpty() || m.isEmpty() || p.isEmpty())
            Toast.makeText(student_registration.this, "Fill all the details", Toast.LENGTH_SHORT).show();
        else {
                auth.createUserWithEmailAndPassword(e, p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    String user = authResult.getUser().getUid();
                    FirebaseStorage fstorage = FirebaseStorage.getInstance();
                    final StorageReference uploader = fstorage.getReference("Image1" + new Random().nextInt(50));
                    uploader.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DocumentReference ref = fstore.collection("Students").document(user);

                                    HashMap<String, Object> stuinfo = new HashMap<>();

                                    Log.d("inside","true21");
                                    stuinfo.put("Full Name", n);
                                    stuinfo.put("USN",u);
                                    stuinfo.put("Email", e);
                                    stuinfo.put("Mobile", m);
                                    stuinfo.put("isProfessor", "0");
                                    stuinfo.put("profile_image", uri.toString());

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
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(student_registration.this, "Image upload failed", Toast.LENGTH_SHORT).show();
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
    }
}