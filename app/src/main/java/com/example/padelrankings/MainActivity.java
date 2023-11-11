package com.example.padelrankings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button createNewPlayerButton;
    private Button showCurrentRankingButton;
    private Button addPlaySessionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNewPlayerButton = findViewById(R.id.main_activity_create_player);
        showCurrentRankingButton = findViewById(R.id.main_activity_show_current_rankings);
        addPlaySessionButton = findViewById(R.id.main_activity_add_play_session);


        createNewPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PlayerActivity.class));
            }
        });
        showCurrentRankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowRankActivity.class));
            }
        });
    }

}