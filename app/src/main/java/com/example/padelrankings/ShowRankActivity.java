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

public class ShowRankActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private SimpleCursorAdapter userAdapter;

    ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rank);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        userList = findViewById(R.id.activity_show_rank_list);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_NAME, null);
        String[] headers = new String[]{DatabaseHelper.COLUMN_NICK, DatabaseHelper.COLUMN_CURRENT_RANKING};
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        userList.setAdapter(userAdapter);
    }
}