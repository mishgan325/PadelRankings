package com.example.padelrankings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlaySessionSettingsActivity extends AppCompatActivity {
    private int selectedPlayersCount;
    private Spinner[] playerSpinners = new Spinner[8];


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_session_setings);

        playerSpinners[0] = findViewById(R.id.activity_pss_1player);
        playerSpinners[1] = findViewById(R.id.activity_pss_2player);
        playerSpinners[2] = findViewById(R.id.activity_pss_3player);
        playerSpinners[3] = findViewById(R.id.activity_pss_4player);
        playerSpinners[4] = findViewById(R.id.activity_pss_5player);
        playerSpinners[5] = findViewById(R.id.activity_pss_6player);
        playerSpinners[6] = findViewById(R.id.activity_pss_7player);
        playerSpinners[7] = findViewById(R.id.activity_pss_8player);

        for (int i = 0; i < playerSpinners.length; i++) {
            playerSpinners[i].setVisibility(View.GONE);
        }

        showPlayersCountDialog();
    }

    private void showPlayersCountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_players_count, null);
        builder.setView(dialogView);

        Spinner spinner = dialogView.findViewById(R.id.spinnerPlayersCount);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.player_counts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedPlayersCount = Integer.parseInt(parentView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                goHome();
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            loadPlayersList();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            goHome();
        });

        builder.show();
    }

    private void loadPlayersList() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<String> nickList = dbHelper.getNicks();
        dbHelper.close();

        if (selectedPlayersCount > nickList.size()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Недостаточно игроков в БД")
                    .setPositiveButton("OK", (dialog, which) -> {
                        goHome();
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        updatePlayerSpinners(nickList);
    }

    private void updatePlayerSpinners(ArrayList nickList) {
        for (int i = 0; i < playerSpinners.length; i++) {
            if (i < selectedPlayersCount) {
                // Отобразить Spinner и установить ему данные
                playerSpinners[i].setVisibility(View.VISIBLE);

                // Создайте адаптер и установите его
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nickList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                playerSpinners[i].setAdapter(adapter);
            } else {
                // Скрыть Spinner
                playerSpinners[i].setVisibility(View.GONE);
            }
        }
    }

    private ArrayList<String> getPlayersList() {
        ArrayList<String> playersList = new ArrayList<>();
        for (int i = 0; i < selectedPlayersCount; i++) {
            playersList.add(playerSpinners[i].getSelectedItem().toString());
        }
        return playersList;
    }

    public void startGame(View view) {
        ArrayList<String> players = getPlayersList();
        Log.d("Player list", players.toString());
        Intent newGame = new Intent(PlaySessionSettingsActivity.this, PlaySessionGamesActivity.class);
        newGame.putExtra("players", new ArrayList<>(players));
        startActivity(newGame);
    }


    private void goHome() {
        Intent intent = new Intent(PlaySessionSettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
