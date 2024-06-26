package ru.mishgan325.padelrankings.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.mishgan325.padelrankings.utils.DatabaseHelper;
import ru.mishgan325.padelrankings.utils.PlayerDBHelper;
import ru.mishgan325.padelrankings.R;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    EditText nickBox;
    EditText nameBox;
    EditText rankBox;
    Button delButton;
    Button saveButton;

    DatabaseHelper databaseHelper;
    PlayerDBHelper playerDBHelper;
    long userId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        nickBox = findViewById(R.id.activity_player_nick_input);
        nameBox = findViewById(R.id.activity_player_name_input);
        rankBox = findViewById(R.id.activity_player_rank_input);
        delButton = findViewById(R.id.activity_player_deleteButton);
        saveButton = findViewById(R.id.activity_player_saveButton);

        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        if (userId > 0) {
            Cursor userCursor = databaseHelper.getWritableDatabase().rawQuery("select * from " + DatabaseHelper.TABLE_NAME + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            nickBox.setText(userCursor.getString(1));
            nameBox.setText(userCursor.getString(2));
            rankBox.setText(String.valueOf(userCursor.getInt(3)));
            userCursor.close();
        } else {
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        if (userId > 0) {
            databaseHelper.updatePlayer(nickBox.getText().toString(), nameBox.getText().toString(), Integer.parseInt(rankBox.getText().toString()), userId);
        } else {
            playerDBHelper = new PlayerDBHelper(this);
            ArrayList<String> playersList =databaseHelper.getNicks();
            for (String player : playersList) {
                playerDBHelper.addPartnerToPlayer(player, nickBox.getText().toString());
            }

            databaseHelper.addPlayer(nickBox.getText().toString(), nameBox.getText().toString(), Integer.parseInt(rankBox.getText().toString()));
        }
        goHome();
    }
    public void delete(View view){
        databaseHelper.deletePlayerById(userId);
        goHome();
    }
    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}