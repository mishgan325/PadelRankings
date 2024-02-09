package com.example.padelrankings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatsActivity extends AppCompatActivity {

    long playerID;
    String playerNICK;

    private PlayerDBHelper playerDBHelper;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);

        title = findViewById(R.id.activity_player_stats_title);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerID = extras.getLong("id");
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        playerNICK = dbHelper.findNicknameById(playerID);

        title.setText("Статистика " + playerNICK);

        playerDBHelper = new PlayerDBHelper(this);

//        fillTestData();

        displayData();

    }


    private void displayData() {
        // Получение ссылки на TableLayout
        TableLayout tableLayout = findViewById(R.id.activity_player_stats_table);

        ArrayList<PlayerPartnerInfo> playerPartnerInfo = playerDBHelper.getPlayerPartnersInfo(playerNICK);
        for (PlayerPartnerInfo info : playerPartnerInfo) {


            TableRow row = new TableRow(this);


            String partner = info.getPartner();
            int games = info.getGames();
            int wins = info.getWins();
            int wonPoints = info.getWonPoints();
            int lostPoints = info.getLostPoints();
            int tiebreaks = info.getTiebreaks();
            int tiebreakWins = info.getTiebreakWins();

            TextView textView1 = createTextView(partner);
            TextView textView2 = createTextView(String.valueOf(games));
            TextView textView3;
            if (games != 0) {
                textView3 = createTextView(String.format("%.0f%%", ((double) wins / games) * 100));
            } else {
                textView3 = createTextView("0");
            }
            TextView textView4 = createTextView(wonPoints + "-" + lostPoints);
            TextView textView5 = createTextView(String.valueOf(tiebreaks));
            TextView textView6;
            if (tiebreaks != 0) {
                textView6 = createTextView(String.format("%.0f%%", ((double) tiebreakWins / tiebreaks) * 100));
            } else {
                textView6 = createTextView("0");
            }

            row.addView(textView1);
            row.addView(textView2);
            row.addView(textView3);
            row.addView(textView4);
            row.addView(textView5);
            row.addView(textView6);

            tableLayout.addView(row);
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textView.setGravity(android.view.Gravity.CENTER);
        return textView;
    }
}
