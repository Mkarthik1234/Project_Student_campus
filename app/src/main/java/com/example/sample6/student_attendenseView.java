package com.example.sample6;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class student_attendenseView extends AppCompatActivity {
    String[] date, absent;
    int count1 = 0, count2 = 0;
    int NUMBER_OF_CLASSES, NUMBER_OF_ADAYS;

    RecyclerView recyclerView;
    adapter_attendenseView adapter;


    FirebaseFirestore fstore;
    FirebaseAuth auth;
    String user;

    String SUBNAME;

    TextView percentage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendense_view);

        percentage = findViewById(R.id.percentage_stuattendanseView);

        SUBNAME = getIntent().getStringExtra("subjectName");

        recyclerView = findViewById(R.id.recyclerView_stuattendenseView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        date = new String[20];
        absent = new String[20];
        adapter = new adapter_attendenseView(student_attendenseView.this, date, absent);

        recyclerView.setAdapter(adapter);

        display_data();


    }

    private int display_data() {
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser().getUid();
        Log.d("user", user);

        fstore.collection("Class").document("Course").collection("subjects")
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("error1 : ", error.getMessage());
                            Log.d("count1", Integer.toString(NUMBER_OF_ADAYS));
                            Log.d("countClass1", Integer.toString(NUMBER_OF_CLASSES));
                            return;
                        }
                        else {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    if (dc.getDocument().get("name").equals(SUBNAME)) {
                                        String id = dc.getDocument().getId();
                                        Log.d("Id", id);

                                        fstore.collection("Class").document("Course").collection("subjects").document(id).collection("Attendense").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                NUMBER_OF_CLASSES = queryDocumentSnapshots.size();
                                                Log.d("countClass", Integer.toString(NUMBER_OF_CLASSES));
                                            }
                                        });

                                        fstore.collection("Class").document("Course").collection("subjects").document(id).collection("Attendense").
                                                addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                        if (error != null) {
                                                            Log.d("error2 : ", error.getMessage());
                                                            return;
                                                        }
                                                        for (DocumentChange dc : value.getDocumentChanges()) {
                                                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                                                String ID = dc.getDocument().getId();
                                                                try {
                                                                    ArrayList a = (ArrayList) dc.getDocument().get("Absent");

                                                                    if (a.contains(user)) {
                                                                        date[count1++] = ID;
                                                                        absent[count2++] = "Absent";
                                                                    }
                                                                } catch (NullPointerException e) {
                                                                    Toast.makeText(student_attendenseView.this, "Network error", Toast.LENGTH_SHORT).show();
                                                                } catch (ClassCastException e) {
                                                                    Toast.makeText(student_attendenseView.this, "Network error", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            adapter.notifyDataSetChanged();
                                                        }
                                                        NUMBER_OF_ADAYS = count2;
                                                        Log.d("count", Integer.toString(NUMBER_OF_ADAYS));
                                                        Log.d("countClass", Integer.toString(NUMBER_OF_CLASSES));

                                                    }
                                                });
                                    }
                                }

                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                });
       return NUMBER_OF_CLASSES-NUMBER_OF_ADAYS;
    }
}