package com.example.attendancetracker;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {
    private List<TimetableItem> timetableData;

    public TimetableAdapter(List<TimetableItem> timetableData) {
        this.timetableData = timetableData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_cell_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimetableItem item = timetableData.get(position);

        holder.dayTextView.setText(item.getDay());
        holder.periodTextView.setText(item.getPeriod());
        holder.subjectTextView.setText(item.getSubject());
        holder.locationTextView.setText(item.getLocation());
    }

    @Override
    public int getItemCount() {
        return timetableData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;
        TextView periodTextView;
        TextView subjectTextView;
        TextView locationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dayTextView = itemView.findViewById(R.id.dayTextView);
            periodTextView = itemView.findViewById(R.id.periodTextView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
        }
    }
}

