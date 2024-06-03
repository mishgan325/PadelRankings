package ru.mishgan325.padelrankings.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import ru.mishgan325.padelrankings.utils.DatabaseHelper;
import ru.mishgan325.padelrankings.utils.PlayerDBHelper;
import ru.mishgan325.padelrankings.entities.PlayerPartnerInfo;
import ru.mishgan325.padelrankings.R;
import ru.mishgan325.padelrankings.adapters.PlayerStatsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlayerStatsActivity extends AppCompatActivity {

    long playerID;
    String playerNICK;

    ArrayList<PlayerPartnerInfo> stats;
    private RecyclerView recyclerViewStats;
    PlayerStatsAdapter adapter;


    private PlayerDBHelper playerDBHelper;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);

        title = findViewById(R.id.activity_player_stats_title);
        recyclerViewStats = findViewById(R.id.activity_player_stats_recyclerView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerID = extras.getLong("id");
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        playerNICK = dbHelper.findNicknameById(playerID);

        title.setText("Статистика " + playerNICK);

        playerDBHelper = new PlayerDBHelper(this);

        stats = playerDBHelper.getPlayerPartnersInfo(playerNICK);

        Collections.sort(stats, new Comparator<PlayerPartnerInfo>() {
            @Override
            public int compare(PlayerPartnerInfo o1, PlayerPartnerInfo o2) {
                // Сначала сравниваем по полю wins
                int winsComparison = Double.compare(o2.getDoubleWinrate(), o1.getDoubleWinrate());

                // Если wins равны, сравниваем по полю games
                if (winsComparison == 0) {
                    return Integer.compare(o2.getGames(), o1.getGames());
                }

                return winsComparison;
            }
        });

        adapter = new PlayerStatsAdapter(this, stats);

        recyclerViewStats.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStats.setAdapter(adapter);

        recyclerViewStats.addItemDecoration(new DividerItemDecoration(recyclerViewStats.getContext(), DividerItemDecoration.VERTICAL));

    }
}
