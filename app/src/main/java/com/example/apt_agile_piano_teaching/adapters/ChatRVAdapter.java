package com.example.apt_agile_piano_teaching.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.models.Chat;

import java.util.ArrayList;

public class ChatRVAdapter extends RecyclerView.Adapter {


    private ArrayList<Chat> chats;
    private Context context;

    public ChatRVAdapter(ArrayList<Chat> chats, Context context) {
        this.chats = chats;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_rv_item, parent, false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg_rv_item, parent, false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch (chats.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        switch (chat.getSender()) {
            case "user":
                ((UserViewHolder)holder).userTv.setText(chat.getMessage());
                break;
            case "bot":
                ((BotViewHolder)holder).botTv.setText(chat.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        TextView userTv;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userTv = itemView.findViewById(R.id.idTVUser);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {

        TextView botTv;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botTv = itemView.findViewById(R.id.idTVBot);
        }
    }

}
