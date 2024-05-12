package com.example.padelrankings;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ShowRankActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private static final String PREFS_NAME = "SavedPrefs";
    private DatabaseHelper databaseHelper;
    private PlayerDBHelper playerDBHelper;
    private ShowRankAdapter userAdapter;
    private Button importButton;
    private Button exportButton;

    private ListView userList;

    private List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rank);

        databaseHelper = new DatabaseHelper(this);
        playerDBHelper = new PlayerDBHelper(this);
        userList = findViewById(R.id.activity_show_rank_list);
        importButton = findViewById(R.id.activity_show_rank_importButton);
        exportButton = findViewById(R.id.activity_show_rank_exportButton);

    }

    @Override
    public void onResume() {
        super.onResume();

        playerList = databaseHelper.getPlayers();
        for (Player i:playerList
        ) {
            Log.d("player", i.toString());
        }
        userAdapter = new ShowRankAdapter(this, R.layout.player_list_item_layout, playerList);

        userList = findViewById(R.id.activity_show_rank_list);

        userList.setAdapter(userAdapter);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаем объект Player, соответствующий нажатому элементу списка
                Player clickedPlayer = playerList.get(position);

                // Получаем id нажатого игрока
                long playerId = clickedPlayer.getId();

                Log.d("Player id:", String.valueOf(playerId));

                Intent intent = new Intent(ShowRankActivity.this, PlayerStatsActivity.class);
                intent.putExtra("id", playerId);
                startActivity(intent);
            }
        });
    }

    public void importData(View view) {
        showSelectionDialog();
    }

    private void showSelectionDialog() {
        final String[] categories = {"Rankings", "PlayerStats"}; // Ваши категории

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите категорию для импорта")
                .setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Обработка выбора категории
                        savePreferences(ShowRankActivity.this, "category", categories[which]);
                        startImport();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startImport() {
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

                String fileContent = FileHelper.readFileContent(this, uri);

                String importCategory = loadPreferences(this, "category");
                if (importCategory.equals("Rankings")) {
                    if (databaseHelper.importData(fileContent)) {
                        recreate();
                    } else {
                        Toast.makeText(this, "При импорте в категорию Rankings возникла ошибка", Toast.LENGTH_SHORT).show();
                    }
                } else if (importCategory.equals("PlayerStats")) {
                    if (playerDBHelper.importData(fileContent)) {
                        recreate();
                    } else {
                        Toast.makeText(this, "При импорте в категорию PlayerStats возникла ошибка", Toast.LENGTH_SHORT).show();
                    }
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
        String fileName1 = "Rankings_" + formattedDate + ".txt";
        String fileName2 = "PlayerStats_" + formattedDate + ".txt";


        if (databaseHelper.exportData(fileName1) && playerDBHelper.exportData(fileName2)) {
            Toast.makeText(this, "БД экспортирована успешно", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "При экспорте возникла ошибка", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePreferences(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String loadPreferences(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, null); // Второй параметр - значение по умолчанию, если ключ не найден
    }
}