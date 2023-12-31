package com.example.padelrankings;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ShowRankActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor userCursor;
    private SimpleCursorAdapter userAdapter;
    private Button importButton;
    private Button exportButton;

    ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rank);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        userList = findViewById(R.id.activity_show_rank_list);
        importButton = findViewById(R.id.activity_show_rank_importButton);
        exportButton = findViewById(R.id.activity_show_rank_exportButton);

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

        userCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " ORDER BY " + DatabaseHelper.COLUMN_CURRENT_RANKING + " DESC", null);
        String[] headers = new String[]{DatabaseHelper.COLUMN_NICK, DatabaseHelper.COLUMN_CURRENT_RANKING};
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        userList.setAdapter(userAdapter);
    }

    public void importData(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        intent.setDataAndType(uri, "text/plain");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String filePath = uri.getPath();  // Получить путь к выбранному файлу

                if (databaseHelper.importData(filePath.replace("/document/raw:", ""))) {
                    Toast.makeText(this, "Импорт произведен успешно", Toast.LENGTH_SHORT).show();
                    recreate();
                } else {
                    Toast.makeText(this, "При импорте возникла ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void exportData(View view) {
        Date currentDate = new Date();

        // Форматируем дату в строку с помощью SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formattedDate = dateFormat.format(currentDate);

        // Создаем название файла с текущей датой
        String fileName = "PadelRankingsExported_" + formattedDate + ".txt";

        if (databaseHelper.exportData(fileName)) {
            Toast.makeText(this, "БД экспортирована успешно", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "При экспорте возникла ошибка", Toast.LENGTH_SHORT).show();
        }
    }
}