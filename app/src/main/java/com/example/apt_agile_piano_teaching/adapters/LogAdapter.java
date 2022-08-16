package com.example.apt_agile_piano_teaching.adapters;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apt_agile_piano_teaching.databinding.LogItemBinding;
import com.example.apt_agile_piano_teaching.models.UserLogTemplate;

import java.util.ArrayList;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogHolder> {

    Context context;
    ArrayList<UserLogTemplate> userLogs;

    public LogAdapter(Context context, ArrayList<UserLogTemplate> userLogs) {
        this.context = context;
        this.userLogs = userLogs;
    }

    @NonNull
    @Override
    public LogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LogItemBinding logItemBinding = LogItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new LogHolder(logItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LogHolder holder, int position) {
        holder.setLogData(userLogs.get(position));
    }

    @Override
    public int getItemCount() {
        return userLogs.size();
    }

    class LogHolder extends RecyclerView.ViewHolder  {

        LogItemBinding binding;

        public LogHolder(@NonNull LogItemBinding logItemBinding) {
            super(logItemBinding.getRoot());
            binding = logItemBinding;
        }

        void setLogData(UserLogTemplate userLogTemplate) {
            binding.showUser.setText(userLogTemplate.getEmail());
            binding.showAction.setText(userLogTemplate.getAction());
            binding.showCategory.setText(userLogTemplate.getCategory());
            binding.showMessage.setText(userLogTemplate.getMessage());
        }
    }
}
