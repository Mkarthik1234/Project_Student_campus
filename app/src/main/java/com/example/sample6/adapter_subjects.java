package com.example.sample6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter_subjects extends RecyclerView.Adapter<adapter_subjects.myViewHolder> {
    Context context;
    ArrayList<subject_javaClass> arrayList;

    public adapter_subjects(Context context, ArrayList arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.singleitem_stusubjects,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        subject_javaClass subject = arrayList.get(position);
        holder.SubName.setText(subject.name);
        holder.SubDescription.setText(subject.description);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView SubName,SubDescription;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            SubName = itemView.findViewById(R.id.subjectName_singleitemSubjects);
            SubDescription = itemView.findViewById(R.id.subjectDescription_singleitemSubjects);
        }
    }
}
