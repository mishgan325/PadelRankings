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

public class PlayerActivity extends AppCompatActivity {

    EditText nickBox;
    EditText nameBox;
    EditText rankBox;
    Button delButton;
    Button saveButton;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
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

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        if (userId > 0) {
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME + " where " +
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
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NICK, nickBox.getText().toString());
        cv.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper.COLUMN_CURRENT_RANKING, Integer.parseInt(rankBox.getText().toString()));

        if (userId > 0) {
            db.update(DatabaseHelper.TABLE_NAME, cv, DatabaseHelper.COLUMN_ID + "=" + userId, null);
        } else {
            db.insert(DatabaseHelper.TABLE_NAME, null, cv);
        }
        goHome();
    }
    public void delete(View view){
        db.delete(DatabaseHelper.TABLE_NAME, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}