package com.example.attendancetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private List<Subject> subjectList;
    private LayoutInflater inflater;

    public SubjectAdapter(Context context, List<Subject> subjects) {
        this.subjectList = subjects;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.d_s, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.subjectNameTextView.setText(subject.getSubjectName());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.subjectNameTextView);
        }
    }
}

