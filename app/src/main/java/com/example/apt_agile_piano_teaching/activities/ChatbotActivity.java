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

        chats.add(new Chat("Ciao collega, sono a tua disposizione! Digita 'aiuto' per ottenere la lista di comandi!", BOT_KEY));
        adapter.notifyDataSetChanged();

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
        if (message.toLowerCase().equals("accordo maggiore")) {
            chats.add(new Chat("La triade di accordo maggiore " +
                    "è composta da 3 note, la prima è la nota selezionata (ad esempio LA), " +
                    "la seconda si calcola sommando 3 semitoni alla prima e la terza 3 semitoni a partire dalla seconda", BOT_KEY));
            adapter.notifyDataSetChanged();
        } else if (message.toLowerCase().equals("ciao")) {
            chats.add(new Chat("Ciao collega, sono a tua disposizione! Digita 'aiuto' per ottenere la lista di comandi!", BOT_KEY));
            adapter.notifyDataSetChanged();
        } else if (message.toLowerCase().equals("accordo minore")) {
            chats.add(new Chat("La triade di accordo minore è composta da 3 note, la prima è la nota di riferimento (ad esempio LA)"
            + "la seconda si calcola sommando 2 semitoni alla prima e la terza 4 semitoni a partire dalla seconda", BOT_KEY));
            adapter.notifyDataSetChanged();
        } else if (message.toLowerCase().equals("semitono")) {
            chats.add(new Chat("Un semitono rappresenta il più piccolo intervallo praticato nel sistema musicale moderno, " +
                    "equivalente alla metà di un tono; può essere diatonico (e corrisponde allora a un intervallo di seconda: per es. mi-fa ) " +
                    "o cromatico (quando unisce una nota alla sua alterazione: per es. do-do diesis ; do-do bemolle ).", BOT_KEY));
            adapter.notifyDataSetChanged();
        } else if (message.toLowerCase().equals("scala")) {
            chats.add(new Chat("Nella teoria musicale, una scala è una successione di suoni nell'ambito di un'ottava, di cui l'ultimo è una ripetizione " +
                    "del primo esattamente un'ottava sopra." +
                    " Si chiama scala ascendente una scala in cui l'altezza delle note cresce, e scala discendente una in cui l'ordine è decrescente." +
                    " Una stessa scala può presentare alterazioni differenti se considerata nel tratto ascendente o in quello discendente.", BOT_KEY));
        } else if (message.toLowerCase().equals("aiuto")) {
            chats.add(new Chat("Digita i seguenti comandi per approfondire gli argomenti citati: \n" +
                    "'accordo maggiore' \n" +
                    "'accordo minore' \n" +
                    "'semitono' \n"
                    + "'scala'", BOT_KEY));
        }
        else {
            chats.add(new Chat("Mi spiace collega, non sono in grado di decifrare il messaggio che hai inviato, digita help per avere informazioni", BOT_KEY));
            adapter.notifyDataSetChanged();
        }
    }
}