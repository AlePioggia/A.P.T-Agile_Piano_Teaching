package com.example.apt_agile_piano_teaching.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apt_agile_piano_teaching.databinding.AssignmentItemBinding;
import com.example.apt_agile_piano_teaching.listeners.AssignmentListener;
import com.example.apt_agile_piano_teaching.models.Assignment;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentHolder>{

    private Context context;
    private AssignmentListener assignmentListener;
    private ArrayList<Assignment> assignments;

    public AssignmentAdapter(Context context, ArrayList<Assignment> assignments, AssignmentListener assignmentListener) {
        this.context = context;
        this.assignments = assignments;
        this.assignmentListener = assignmentListener;
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
            binding.assignmentExercise.setText(assignment.getExercise());
            binding.assignmentPagesAndBook.setText(assignment.getBookName() + " pg: " + assignment.getPages());
            binding.assignmentBpm.setText(String.valueOf(assignment.getBpm()));
            Glide.with(context).load(FirebaseStorage.getInstance().getReference("templates/assignment.jpg")).into(binding.assignmentImageView);
            binding.deleteAssignment.setOnClickListener(v -> assignmentListener.onAssignmentClicked(assignment));
        }

    }
}
