package com.example.apt_agile_piano_teaching.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.adapters.ChatRVAdapter;
import com.example.apt_agile_piano_teaching.databinding.ActivityChatbotBinding;
import com.example.apt_agile_piano_teaching.databinding.ActivityLessonsBinding;
import com.example.apt_agile_piano_teaching.models.Chat;

import java.util.ArrayList;

public class ChatbotActivity extends AppCompatActivity {

    private ActivityChatbotBinding binding;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<Chat> chats;
    private ChatRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatbotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chats = new ArrayList<>();
        adapter = new ChatRVAdapter(chats, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(manager);
        binding.chatRecyclerView.setAdapter(adapter);

        binding.idFABSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.idEdtMessage.getText().toString().isEmpty()) {
                    Toast.makeText(ChatbotActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(binding.idEdtMessage.getText().toString());
                binding.idEdtMessage.setText("");
            }
        });
    }

    private void getResponse(String message) {
        chats.add(new Chat(message, USER_KEY));
        adapter.notifyDataSetChanged();
        if (message.equals("accordo maggiore")) {
            chats.add(new Chat("La triade di accordo maggiore " +
                    "è composta da 3 note, la prima è la nota selezionata (ad esempio LA), " +
                    "la seconda si calcola sommando 2 semitoni e la terza 3 semitoni a partire dalla seconda", BOT_KEY));
            adapter.notifyDataSetChanged();
        } else {
            chats.add(new Chat("Mi spiace collega, non sono in grado di decifrare il messaggio che hai inviato, digita help per avere informazioni", BOT_KEY));
            adapter.notifyDataSetChanged();
        }
    }
}