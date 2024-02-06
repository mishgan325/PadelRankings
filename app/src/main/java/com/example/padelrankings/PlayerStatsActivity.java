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

public class PlayerStatsActivity extends AppCompatActivity {

    long playerID;
    String playerNICK;

    private PlayerDBHelper playerDBHelper;
    private SQLiteDatabase database;

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
        database = playerDBHelper.getWritableDatabase();

        fillTestData();

        displayData();

    }

    private void fillTestData() {
        // Пробные данные
        String[] nicknames = {"Player1", "Player2", "Player3"};
        String[] partners = {"Partner1", "Partner2", "Partner3"};
        int[] games = {10, 15, 20};
        int[] wins = {8, 10, 12};
        int[] wonPoints = {100, 150, 200};
        int[] lostPoints = {50, 70, 90};
        int[] tiebreaks = {2, 3, 4};
        int[] tiebreakWins = {1, 2, 3};

        for (int i = 0; i < nicknames.length; i++) {
            ContentValues values = new ContentValues();
            values.put("nickname", nicknames[i]);
            values.put("partner", partners[i]);
            values.put("games", games[i]);
            values.put("wins", wins[i]);
            values.put("won_points", wonPoints[i]);
            values.put("lost_points", lostPoints[i]);
            values.put("tiebreaks", tiebreaks[i]);
            values.put("tiebreak_wins", tiebreakWins[i]);
            database.insert("PlayerStats", null, values);
        }
    }

    private void displayData() {
        // Получение ссылки на TableLayout
        TableLayout tableLayout = findViewById(R.id.activity_player_stats_table);

        // Получение данных из БД
        Cursor cursor = database.rawQuery("SELECT * FROM PlayerStats", null);

        // Проход по результатам запроса
        while (cursor.moveToNext()) {
            // Создание новой строки
            TableRow row = new TableRow(this);

            // Получение данных из курсора
            String nickname = cursor.getString(cursor.getColumnIndexOrThrow("nickname"));
            String partner = cursor.getString(cursor.getColumnIndexOrThrow("partner"));
            int games = cursor.getInt(cursor.getColumnIndexOrThrow("games"));
            int wins = cursor.getInt(cursor.getColumnIndexOrThrow("wins"));
            int wonPoints = cursor.getInt(cursor.getColumnIndexOrThrow("won_points"));
            int lostPoints = cursor.getInt(cursor.getColumnIndexOrThrow("lost_points"));
            int tiebreaks = cursor.getInt(cursor.getColumnIndexOrThrow("tiebreaks"));
            int tiebreakWins = cursor.getInt(cursor.getColumnIndexOrThrow("tiebreak_wins"));

            TextView textView1 = createTextView(partner);
            TextView textView2 = createTextView(String.valueOf(games));
            TextView textView3 = createTextView(String.valueOf(wins/games));
            TextView textView4 = createTextView(wonPoints + "-" + lostPoints);
            TextView textView5 = createTextView(String.valueOf(tiebreaks));
            TextView textView6 = createTextView(String.valueOf(tiebreakWins/tiebreaks));

            row.addView(textView1);
            row.addView(textView2);
            row.addView(textView3);
            row.addView(textView4);
            row.addView(textView5);
            row.addView(textView6);

            tableLayout.addView(row);
        }

        cursor.close();
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textView.setGravity(android.view.Gravity.CENTER);
        return textView;
    }
}
