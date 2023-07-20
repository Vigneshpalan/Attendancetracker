package com.example.attendancetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Subject> subjectList;
    private OnItemClickListener listener;

    public RecyclerViewAdapter(Context context, List<Subject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.textSubjectName.setText(subject.getSubjectName());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textSubjectName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textSubjectName = itemView.findViewById(R.id.subjectNameTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        Subject subject = subjectList.get(position);
                        listener.onItemClick(subject);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Subject subject);
    }
}


