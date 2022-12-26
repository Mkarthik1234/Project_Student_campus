package com.example.sample6;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class student_attendense_subjectView extends AppCompatActivity {

    ArrayList<subject_javaClass> subject;

    RecyclerView recyclerView;
    adapter_attendenseSubject adapter;

    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendense_subject_view);

        recyclerView = findViewById(R.id.recyclerview_stuattendense);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        subject = new ArrayList<subject_javaClass>();
        adapter = new adapter_attendenseSubject(student_attendense_subjectView.this,subject);

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