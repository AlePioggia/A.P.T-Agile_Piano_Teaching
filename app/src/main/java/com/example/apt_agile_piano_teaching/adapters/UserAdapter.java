package com.example.apt_agile_piano_teaching.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apt_agile_piano_teaching.databinding.CardBinding;
import com.example.apt_agile_piano_teaching.models.User;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private final List<User> users;
    private final Context context;

    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardBinding binding = CardBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {

        CardBinding binding;

        public UserHolder(CardBinding cardBinding) {
            super(cardBinding.getRoot());
            binding = cardBinding;
        }

        void setUserData(User user) {
            binding.studentTextView.setText(user.getName() + " " + user.getLastName());
            Glide.with(context).load(FirebaseStorage.getInstance().getReference("uploads/" + user.getMail() + ".jpg")).into(binding.studentImageView);
        }
    }

}
