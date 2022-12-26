package com.example.sample6;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter_attendenseSubject extends RecyclerView.Adapter<adapter_attendenseSubject.myViewHolder> {
    Context context;
    ArrayList<subject_javaClass> arrayList;

    public adapter_attendenseSubject() {
    }

    public adapter_attendenseSubject(Context context, ArrayList<subject_javaClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.singleitem_stuattendensesubject,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_attendenseSubject.myViewHolder holder, int position) {
        subject_javaClass subject = arrayList.get(position);
        holder.subName.setText(subject.name);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,student_attendenseView.class);
                intent.putExtra("subjectName",subject.name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView subName;
        Button view;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            subName = itemView.findViewById(R.id.subjectName_singleitemAttendenseSubject);
            view = itemView.findViewById(R.id.viewAttendanse_singleitemAttendanseSubject);
        }
    }
}
