package com.example.padelrankings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class PlaySessionCheckActivity extends AppCompatActivity {

    private ArrayList<GameData> games = new ArrayList<>();
    private ArrayList<String> players = new ArrayList<>();

    private RecyclerView checkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_session_check);

        Intent intent = getIntent();
        games = (ArrayList<GameData>) intent.getSerializableExtra("games");
        players = intent.getStringArrayListExtra("players");

        checkList = findViewById(R.id.activity_check_recyclerView);

        CheckSessionAdapter adapter = new CheckSessionAdapter(this, games);

        adapter.setNickList(players);

        checkList.setLayoutManager(new LinearLayoutManager(this));
        checkList.setAdapter(adapter);

    }

    private ArrayList<GameData> parseData() {
        ArrayList<GameData> parsedGames = new ArrayList<>();
        for (int i = 0; i < checkList.getChildCount(); i++) {
            View view = checkList.getChildAt(i);
            Spinner player1 = view.findViewById(R.id.activity_check_team1player1);
            Spinner player2 = view.findViewById(R.id.activity_check_team1player2);
            Spinner player3 = view.findViewById(R.id.activity_check_team2player1);
            Spinner player4 = view.findViewById(R.id.activity_check_team2player2);
            EditText score1 = view.findViewById(R.id.activity_check_score1);
            EditText score2 = view.findViewById(R.id.activity_check_score2);

            String p1, p2, p3, p4;
            int sc1, sc2;
            p1 = player1.getSelectedItem().toString();
            p2 = player2.getSelectedItem().toString();
            p3 = player3.getSelectedItem().toString();
            p4 = player4.getSelectedItem().toString();
            sc1 = Integer.parseInt(score1.getText().toString());
            sc2 = Integer.parseInt(score2.getText().toString());

            parsedGames.add(new GameData(p1, p2, p3, p4, sc1, sc2));
        }

        return parsedGames;
    }

    public void finishSession(View view) {
        games = parseData();

        for (int i = 0; i < games.size(); i++) {
            Log.d("finalResults", games.get(i).returnData());
        }
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<Integer> ranks = new ArrayList<>();
        for(String player: players) {
             ranks.add(dbHelper.getRankByNick(player));
        }

        GameSession gameSession = new GameSession(players, ranks);

        for(GameData game: games) {
            gameSession.runGame(game);
        }
        ArrayList<Player> finalPlayers = gameSession.getPlayers();

        loadToBD(finalPlayers, dbHelper);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    private void loadToBD(ArrayList<Player> finalPlayers, DatabaseHelper dbHelper) {
        for (Player player: finalPlayers) {
            Log.d("final", player.getNick() + " " + player.getRank());
            dbHelper.updateUserRank(player.getNick(), player.getRank());
        }
    }

}
