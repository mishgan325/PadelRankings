package com.example.padelrankings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class PlaySessionGameActivity extends AppCompatActivity {

    private Spinner team1player1spinner;
    private Spinner team1player2spinner;
    private Spinner team2player1spinner;
    private Spinner team2player2spinner;
    private EditText team1score;
    private EditText team2score;
    private Button submitButton;
    private Button escapeButton;
    private TextView currentGame;
    private int numberOfGame = 1;

    ArrayList<String> nicks = new ArrayList<>();

    ArrayList<GameData> games = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_session_game);

        Intent intent = getIntent();

        nicks = intent.getStringArrayListExtra("players");

        Log.d("Players", nicks.toString());

        team1player1spinner = findViewById(R.id.activity_check_team1player1);
        team1player2spinner = findViewById(R.id.activity_check_team1player2);
        team2player1spinner = findViewById(R.id.activity_check_team2player1);
        team2player2spinner = findViewById(R.id.activity_check_team2player2);
        team1score = findViewById(R.id.activity_check_score);
        team2score = findViewById(R.id.activity_psg_team2score);
        submitButton = findViewById(R.id.activity_psg_submitButton);
        escapeButton = findViewById(R.id.activity_psg_escapeButton);
        currentGame = findViewById(R.id.activity_psg_currentGame);
        updatePlayersSpinners();
    }

    private void updatePlayersSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nicks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        team1player1spinner.setAdapter(adapter);
        team1player2spinner.setAdapter(adapter);
        team2player1spinner.setAdapter(adapter);
        team2player2spinner.setAdapter(adapter);
    }



    public void submitScore(View view) {
        int score1 = Integer.parseInt(team1score.getText().toString());
        int score2 = Integer.parseInt(team2score.getText().toString());
        ArrayList<String> players= new ArrayList<>();
        players.add(team1player1spinner.getSelectedItem().toString());
        players.add(team1player2spinner.getSelectedItem().toString());
        players.add(team2player1spinner.getSelectedItem().toString());
        players.add(team2player2spinner.getSelectedItem().toString());

        games.add(new GameData(players.get(0), players.get(1), players.get(2), players.get(3), score1, score2));
        numberOfGame++;
        currentGame.setText("Игра " + numberOfGame);
    }

    public void checkSession(View view) {
        Intent intent = new Intent(PlaySessionGameActivity.this, PlaySessionCheckActivity.class);
        intent.putExtra("games", games);
        intent.putExtra("players", nicks);
        startActivity(intent);
    }

    public void escape(View view) {
        Intent intent = new Intent(PlaySessionGameActivity.this, PlaySessionSettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
