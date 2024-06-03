package ru.mishgan325.padelrankings.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mishgan325.padelrankings.utils.DatabaseHelper;
import ru.mishgan325.padelrankings.entities.GameData;
import ru.mishgan325.padelrankings.entities.GameSession;
import ru.mishgan325.padelrankings.entities.Player;
import ru.mishgan325.padelrankings.utils.PlayerDBHelper;
import com.example.padelrankings.R;
import ru.mishgan325.padelrankings.adapters.PlaySessionGamesAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class PlaySessionGamesActivity extends AppCompatActivity {

    private ArrayList<GameData> games = new ArrayList<>();
    private ArrayList<String> players = new ArrayList<>();

    private RecyclerView checkList;
    PlaySessionGamesAdapter adapter;


    // сохранение игр
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateGameData();
        outState.putSerializable("games", games);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_session_games);

        Intent intent = getIntent();

        players = intent.getStringArrayListExtra("players");

        if (savedInstanceState != null) {
            games = (ArrayList<GameData>) savedInstanceState.getSerializable("games");
        } else {
            games = new ArrayList<>();
            games.add(new GameData(players.get(0), players.get(0), players.get(0), players.get(0), 0, 0));
        }

        checkList = findViewById(R.id.activity_check_recyclerView);

        adapter = new PlaySessionGamesAdapter(this, games);

        adapter.setNickList(players);

        checkList.setLayoutManager(new LinearLayoutManager(this));
        checkList.setAdapter(adapter);

    }

    private void updateGameData() {
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

            games.get(i).setTeam1player1(p1);
            games.get(i).setTeam1player2(p2);
            games.get(i).setTeam2player1(p3);
            games.get(i).setTeam2player2(p4);
            games.get(i).setTeam1score(sc1);
            games.get(i).setTeam2score(sc2);

        }
    }

    public void finishSession(View view) {
        updateGameData();

        for (int i = 0; i < games.size(); i++) {
            Log.i("finalResults", games.get(i).toString());

            if (!uniquePlayers(games.get(i)))
            {
                Toast.makeText(this, "Совпадают игроки в игре №" + i+1, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<Integer> ranks = new ArrayList<>();
        for(String player: players) {
             ranks.add(dbHelper.getRankByNick(player));
        }

        GameSession gameSession = new GameSession(players, ranks);

        PlayerDBHelper playerDBHelper = new PlayerDBHelper(this);


        for(GameData game: games) {
            gameSession.runGame(game);
            playerDBHelper.updateGameData(game.getTeam1player1(), game.getTeam1player2(), game.getTeam1score(), game.getTeam2score());
            playerDBHelper.updateGameData(game.getTeam2player1(), game.getTeam2player2(), game.getTeam2score(), game.getTeam1score());
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

    public void addGame(View view) {
        updateGameData();

        for (int i = 0; i < games.size(); i++) {
            Log.d("tmp", games.get(i).toString());
        }

        games.add(new GameData(games.get(games.size()-1)));
        adapter.notifyDataSetChanged();
    }

    public void removeGame(View view) {
        if(games.size() > 1) {
            updateGameData();
            games.remove(games.size()-1);
            adapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(this, "Минимум 1 игра", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean uniquePlayers(GameData game) {
        Set<String> playerSet = new HashSet<>();
        playerSet.add(game.getTeam1player1());
        playerSet.add(game.getTeam1player2());
        playerSet.add(game.getTeam2player1());
        playerSet.add(game.getTeam2player2());

        return playerSet.size() == 4;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ActivityLifecycle", "Activity destroyed");
    }
}
