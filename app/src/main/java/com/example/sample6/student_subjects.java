package com.example.sample6;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class student_subjects extends AppCompatActivity {

    ArrayList<subject_javaClass> subject;

    RecyclerView recyclerView;
    adapter_subjects adapter;

    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subjects);

        recyclerView = findViewById(R.id.recyclerview_stusubjects);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        subject = new ArrayList<subject_javaClass>();
        adapter = new adapter_subjects(student_subjects.this,subject);

        recyclerView.setAdapter(adapter);

        display_data();
    }

    private void display_data() {
        fstore = FirebaseFirestore.getInstance();
        fstore.collection("Class").document("Course").collection("subjects").
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null)
                        {
                            Log.d("error : ",error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges())
                        {
                            if(dc.getType()==DocumentChange.Type.ADDED)
                            {
                                subject.add(dc.getDocument().toObject(subject_javaClass.class));
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }
                });


    }
}