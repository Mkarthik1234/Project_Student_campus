package com.example.sample6;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter_attendenseView extends RecyclerView.Adapter<adapter_attendenseView.myViewHolder> {
    Context context;
    public String[] date, absent;

    public adapter_attendenseView(Context context, String[] date, String[] absent) {
        this.context = context;
        this.date = date;
        this.absent = absent;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.singleitem_attendenseview, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        try {
            holder.date.setText(date[position]);
            holder.pora.setText(absent[position]);
        }catch (Exception e)
        {
            Log.d("Error",e.toString());
        }
        }

        @Override
        public int getItemCount () {
            return date.length + absent.length;
        }

        public class myViewHolder extends RecyclerView.ViewHolder {
            TextView date, pora;

            public myViewHolder(@NonNull View itemView) {
                super(itemView);
                date = itemView.findViewById(R.id.textview_Date_singleitemAttendenseView);
                pora = itemView.findViewById(R.id.textview_PorA_singleitemAttendenseView);
            }
        }
    }
