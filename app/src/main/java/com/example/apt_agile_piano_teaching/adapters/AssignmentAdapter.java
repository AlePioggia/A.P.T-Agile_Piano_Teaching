package com.example.apt_agile_piano_teaching.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apt_agile_piano_teaching.databinding.AssignmentItemBinding;
import com.example.apt_agile_piano_teaching.models.Assignment;

import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentHolder> {

    Context context;
    ArrayList<Assignment> assignments;

    public AssignmentAdapter(Context context, ArrayList<Assignment> assignments) {
        this.context = context;
        this.assignments = assignments;
    }

    @NonNull
    @Override
    public AssignmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AssignmentItemBinding binding = AssignmentItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new AssignmentHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentHolder holder, int position) {
        holder.setAssignmentData(assignments.get(position));
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public class AssignmentHolder extends  RecyclerView.ViewHolder{

        AssignmentItemBinding binding;

        public AssignmentHolder(@NonNull AssignmentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void setAssignmentData(Assignment assignment) {
            binding.showAssignmentExerciseType.setText(assignment.getExercise());
            binding.showAssignmentBook.setText(assignment.getBookName());
            binding.showAssignmentBpm.setText(String.valueOf(assignment.getBpm()));
            binding.showAssignmentPages.setText(assignment.getPages());
        }

    }
}
